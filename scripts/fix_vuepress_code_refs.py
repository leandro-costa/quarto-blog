#!/usr/bin/env python3
"""
Corrige a sintaxe VuePress @[code](caminho) que não é entendida pelo Quarto,
substituindo cada ocorrência pelo conteúdo real do arquivo referenciado,
embutido como bloco de código fenced.

Também repara o padrão quebrado em que o @[code](...) acabou preso dentro
de um atributo title="..." de um callout (bug de migração), restaurando um
título legível e movendo o código para o corpo do callout.
"""
import re
import sys
from pathlib import Path

RAIZ = Path(__file__).resolve().parent.parent
POSTS_DIR = RAIZ / "posts"

CODE_REF_RE = re.compile(r"@\[code\]\(([^)]+)\)")
TITLE_WITH_CODE_RE = re.compile(r'title="@\[code\]\(([^)]+)\)"')


def build_java_index():
    """Mapeia nome_do_arquivo.java -> lista de (Path completo, path_str_lower)."""
    index = {}
    for java_file in POSTS_DIR.rglob("*.java"):
        if "livro" in java_file.parts:
            continue
        index.setdefault(java_file.name, []).append(java_file)
    return index


def escolher_melhor_candidato(nome_arquivo, caminho_referenciado, candidatos):
    """Entre os candidatos com o mesmo nome de arquivo, escolhe o que tem
    mais segmentos de diretório em comum com o caminho referenciado no
    @[code](...) original."""
    if len(candidatos) == 1:
        return candidatos[0]

    ref_partes = set(
        p for p in re.split(r"[/\\]", caminho_referenciado.lower()) if p not in (".", "..", "code")
    )

    def score(candidato: Path):
        cand_partes = set(p.lower() for p in candidato.parts)
        return len(ref_partes & cand_partes)

    candidatos_ordenados = sorted(candidatos, key=score, reverse=True)
    return candidatos_ordenados[0]


def resolver_arquivo(caminho_referenciado, index, arquivo_origem):
    nome = Path(caminho_referenciado).name
    candidatos = index.get(nome)
    if not candidatos:
        return None, f"AVISO: {nome} não encontrado (referenciado em {arquivo_origem})"
    escolhido = escolher_melhor_candidato(nome, caminho_referenciado, candidatos)
    return escolhido, None


def montar_bloco_codigo(caminho_arquivo: Path) -> str:
    conteudo = caminho_arquivo.read_text(encoding="utf-8", errors="replace").rstrip("\n")
    return f"```java\n// {caminho_arquivo.name}\n{conteudo}\n```"


def processar_arquivo(qmd_path: Path, index, avisos: list):
    texto = qmd_path.read_text(encoding="utf-8")
    if "@[code](" not in texto:
        return False

    linhas = texto.split("\n")
    novas_linhas = []

    for linha in linhas:
        m_title = TITLE_WITH_CODE_RE.search(linha)
        if m_title:
            caminho_ref = m_title.group(1)
            arq, aviso = resolver_arquivo(caminho_ref, index, qmd_path)
            if aviso:
                avisos.append(aviso)
            titulo_novo = f'title="Código: {Path(caminho_ref).name}"'
            linha_corrigida = TITLE_WITH_CODE_RE.sub(titulo_novo, linha)
            novas_linhas.append(linha_corrigida)
            if arq:
                novas_linhas.append(montar_bloco_codigo(arq))
            continue

        m_code = CODE_REF_RE.search(linha)
        if m_code and "title=" not in linha:
            caminho_ref = m_code.group(1)
            arq, aviso = resolver_arquivo(caminho_ref, index, qmd_path)
            if aviso:
                avisos.append(aviso)
                novas_linhas.append(linha)  # mantém original se não resolveu
                continue
            novas_linhas.append(montar_bloco_codigo(arq))
            continue

        novas_linhas.append(linha)

    novo_texto = "\n".join(novas_linhas)
    if novo_texto != texto:
        qmd_path.write_text(novo_texto, encoding="utf-8")
        return True
    return False


def main():
    index = build_java_index()
    avisos = []
    arquivos_alterados = []

    for qmd_path in POSTS_DIR.rglob("_content.qmd"):
        if "livro" in qmd_path.parts:
            continue
        if processar_arquivo(qmd_path, index, avisos):
            arquivos_alterados.append(qmd_path.relative_to(RAIZ))

    print(f"Arquivos alterados: {len(arquivos_alterados)}")
    for f in arquivos_alterados:
        print(f"  - {f}")

    if avisos:
        print(f"\nAvisos ({len(avisos)}):")
        for a in avisos:
            print(f"  ! {a}")


if __name__ == "__main__":
    main()
