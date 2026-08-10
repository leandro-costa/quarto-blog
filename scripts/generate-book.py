#!/usr/bin/env python3
"""
Gera automaticamente os capítulos do livro a partir dos posts.
Executado via 'pre-render' no _quarto.yml raiz.
"""
import os
import re
from pathlib import Path

POSTS_DIR = Path("posts")
LIVRO_DIR = Path("livro")


def extract_order(name: str) -> int:
    """Extrai número de ordenação do início do nome da pasta."""
    match = re.match(r'^(\d+)', name)
    return int(match.group(1)) if match else 999


def read_yaml_frontmatter(filepath: Path) -> dict:
    """Lê apenas o front matter YAML de um arquivo .qmd."""
    meta = {}
    if not filepath.exists():
        return meta
    with open(filepath, "r", encoding="utf-8") as f:
        content = f.read()
    if content.startswith("---"):
        parts = content.split("---", 2)
        if len(parts) >= 3:
            for line in parts[1].strip().split("\n"):
                if ":" in line:
                    key, val = line.split(":", 1)
                    meta[key.strip()] = val.strip().strip('"').strip("'")
    return meta


def classify_part(categories: list) -> str | None:
    """Determina a 'part' do livro com base nas categorias."""
    cat_set = set(c.lower() for c in categories)
    if cat_set & {"exercicio", "trabalho", "entrega"}:
        return "Exercícios"
    return None  # sem part = capítulo direto


def main():
    # Encontra todos os posts com _content.qmd
    posts = []
    for content_file in sorted(POSTS_DIR.rglob("_content.qmd"), key=lambda p: extract_order(p.parent.name)):
        rel_path = content_file.relative_to(POSTS_DIR)
        post_name = content_file.parent.name

        # Lê metadados do index.qmd
        index_file = content_file.parent / "index.qmd"
        meta = read_yaml_frontmatter(index_file)

        title = meta.get("title", post_name.replace("_", " ").title())
        date = meta.get("date", "")
        categories = []
        if "categories" in meta:
            cats = meta["categories"]
            # Pode ser lista YAML como [aula, heranca]
            cats = cats.strip("[]")
            categories = [c.strip().strip('"').strip("'") for c in cats.split(",")]

        # Caminho relativo desde livro/ até o _content.qmd
        include_path = f"../posts/{rel_path}"

        part = classify_part(categories)

        posts.append({
            "file": f"{post_name}.qmd",
            "title": title,
            "include_path": include_path,
            "part": part,
            "order": extract_order(post_name),
        })

    # Limpa arquivos .qmd gerados anteriormente no livro/
    # Mantém apenas arquivos fixos: index.qmd, referencias.qmd, glossario.qmd
    fixed_files = {"index.qmd", "referencias.qmd", "glossario.qmd", "_quarto.yml"}
    for f in LIVRO_DIR.glob("*.qmd"):
        if f.name not in fixed_files:
            f.unlink()

    # Gera os arquivos .qmd do livro
    for post in posts:
        qmd_path = LIVRO_DIR / post["file"]
        content = f"""---
title: "{post['title']}"
---

{{{{< include {post['include_path']} >}}}}
"""
        with open(qmd_path, "w", encoding="utf-8") as f:
            f.write(content)

    # Monta a lista de capítulos
    chapters = ["index.qmd"]

    current_part = None
    for post in posts:
        if post["part"] and post["part"] != current_part:
            current_part = post["part"]
            chapters.append({"part": current_part, "chapters": []})
            chapters[-1]["chapters"].append(post["file"])
        elif post["part"] and post["part"] == current_part:
            chapters[-1]["chapters"].append(post["file"])
        else:
            chapters.append(post["file"])
            current_part = None

    chapters.extend(["referencias.qmd", "glossario.qmd"])

    # Gera o _quarto.yml do livro
    # Precisamos preservar o resto da configuração, então usamos um template
    chapters_yaml = ""
    for item in chapters:
        if isinstance(item, dict) and "part" in item:
            chapters_yaml += f"    - part: \"{item['part']}\"\n"
            chapters_yaml += "      chapters:\n"
            for ch in item["chapters"]:
                chapters_yaml += f"        - {ch}\n"
        else:
            chapters_yaml += f"    - {item}\n"

    quarto_yml = f"""project:
  type: book
  output-dir: _book

book:
  title: "Material de Aulas — Turma 2026.1 ADS"
  subtitle: "Programação Orientada a Objetos"
  author: "Leandro Souza"
  date: today
  date-format: "DD/MM/YYYY"
  chapters:
{chapters_yaml.rstrip()}

filters:
  - diagram

lang: pt-BR
bibliography: references.bib
csl: abnt.csl

format:
  typst:
    papersize: a4
    documentclass: book
    toc: true
    toc-title: "Sumário"
    lof: true
    lot: true
    number-sections: true
    fontsize: 12pt
    margin:
      left: 3cm
      top: 3cm
      right: 2cm
      bottom: 2cm
    include-in-header:
      text: |
        #set par(hanging-indent: 0pt)
        #show heading: it => {{
          set par(hanging-indent: 0pt)
          block(it, above: 1.6em, below: 1em)
        }}
        #set par(first-line-indent: 1.25cm, leading: 1em, justify: true)
        #show link: it => underline(text(fill: rgb("#1a56db"), it))

execute:
  freeze: auto
"""

    with open(LIVRO_DIR / "_quarto.yml", "w", encoding="utf-8") as f:
        f.write(quarto_yml)

    print(f"[generate-book] {len(posts)} capítulos gerados no livro/")
    for p in posts:
        part_info = f" (part: {p['part']})" if p["part"] else ""
        print(f"  - {p['file']}: {p['title']}{part_info}")


if __name__ == "__main__":
    main()
