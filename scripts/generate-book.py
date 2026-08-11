#!/usr/bin/env python3
"""
Gera automaticamente os capítulos do livro a partir dos posts.
Executado via 'pre-render' no _quarto.yml raiz.

Copia arquivos compartilhados (references.bib, abnt.csl, _extensions)
para livro/ em vez de usar symlinks.

Tambem gera Lista de Figuras (lof) e Lista de Tabelas (lot) manualmente,
pois o Quarto+Typst tem um bug (issue #14081) onde lof/lot fica vazio.
"""
import os
import re
import shutil
from pathlib import Path

POSTS_DIR = Path("posts")
LIVRO_DIR = Path("livro")
ROOT_DIR = Path(".")


def extract_order(name: str) -> int:
    match = re.match(r"^(\d+)", name)
    return int(match.group(1)) if match else 999


def read_yaml_frontmatter(filepath: Path) -> dict:
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
    cat_set = set(c.lower() for c in categories)
    if cat_set & {"exercicio", "trabalho", "entrega"}:
        return "Exercicios"
    return None


def copy_shared_files():
    src_bib = ROOT_DIR / "references.bib"
    dst_bib = LIVRO_DIR / "references.bib"
    if src_bib.exists():
        shutil.copy2(src_bib, dst_bib)
        print(f"[generate-book] Copiado: {src_bib} -> {dst_bib}")

    src_csl = ROOT_DIR / "abnt.csl"
    dst_csl = LIVRO_DIR / "abnt.csl"
    if src_csl.exists():
        shutil.copy2(src_csl, dst_csl)
        print(f"[generate-book] Copiado: {src_csl} -> {dst_csl}")

    src_ext = ROOT_DIR / "_extensions"
    dst_ext = LIVRO_DIR / "_extensions"
    if src_ext.exists():
        if dst_ext.exists():
            shutil.rmtree(dst_ext)
        shutil.copytree(src_ext, dst_ext)
        print(f"[generate-book] Copiado: {src_ext}/ -> {dst_ext}/")


def extract_floats():
    figuras = []
    tabelas = []

    for content_file in sorted(POSTS_DIR.rglob("_content.qmd"), key=lambda p: extract_order(p.parent.name)):
        post_name = content_file.parent.name
        index_file = content_file.parent / "index.qmd"
        meta = read_yaml_frontmatter(index_file)
        chapter_title = meta.get("title", post_name.replace("_", " ").title())

        with open(content_file, "r", encoding="utf-8") as f:
            content = f.read()

        # Figuras - padrao chunk: //| label: fig-xxx + //| fig-cap: "..."
        for match in re.finditer(
            r"//\|\s*label:\s*(fig-[\w-]+).*?//\|\s*fig-cap:\s*\"([^\"]+)\"",
            content, re.DOTALL
        ):
            figuras.append({
                "label": match.group(1),
                "caption": match.group(2),
                "chapter": chapter_title,
            })

        # Figuras - padrao div: ::: {#fig-xxx}\nCaption\n:::
        for match in re.finditer(
            r"::: \{#(fig-[\w-]+)\}\n([^\n]+?)\n:::",
            content, re.DOTALL
        ):
            figuras.append({
                "label": match.group(1),
                "caption": match.group(2).strip(),
                "chapter": chapter_title,
            })

        # Figuras - padrao markdown: ![caption](url){#fig-xxx}
        for match in re.finditer(
            r"!\[([^\]]*)\]\([^)]+\)\{?#(fig-[\w-]+)?\}?",
            content
        ):
            fid = match.group(2)
            if fid:
                figuras.append({
                    "label": fid,
                    "caption": match.group(1),
                    "chapter": chapter_title,
                })

        # Tabelas - padrao chunk: //| label: tbl-xxx + //| tbl-cap: "..."
        for match in re.finditer(
            r"//\|\s*label:\s*(tbl-[\w-]+).*?//\|\s*tbl-cap:\s*\"([^\"]+)\"",
            content, re.DOTALL
        ):
            tabelas.append({
                "label": match.group(1),
                "caption": match.group(2),
                "chapter": chapter_title,
            })

        # Tabelas - padrao div: ::: {#tbl-xxx}\nCaption\n:::
        for match in re.finditer(
            r"::: \{#(tbl-[\w-]+)\}\n([^\n]+?)\n:::",
            content, re.DOTALL
        ):
            tabelas.append({
                "label": match.group(1),
                "caption": match.group(2).strip(),
                "chapter": chapter_title,
            })

    return figuras, tabelas


def generate_lof_lot(figuras, tabelas):
    lof_lines = ["---", 'title: "Lista de Figuras"', "---", ""]
    if figuras:
        for i, fig in enumerate(figuras, 1):
            lof_lines.append(f"{i}. **{fig['caption']}**  ")
            lof_lines.append(f"   — *{fig['chapter']}*  ")
            lof_lines.append("")
    else:
        lof_lines.append("Nenhuma figura encontrada.")

    with open(LIVRO_DIR / "lof.qmd", "w", encoding="utf-8") as f:
        f.write("\n".join(lof_lines))
    print(f"[generate-book] Gerado: livro/lof.qmd ({len(figuras)} figuras)")

    lot_lines = ["---", 'title: "Lista de Tabelas"', "---", ""]
    if tabelas:
        for i, tbl in enumerate(tabelas, 1):
            lot_lines.append(f"{i}. **{tbl['caption']}**  ")
            lot_lines.append(f"   — *{tbl['chapter']}*  ")
            lot_lines.append("")
    else:
        lot_lines.append("Nenhuma tabela encontrada.")

    with open(LIVRO_DIR / "lot.qmd", "w", encoding="utf-8") as f:
        f.write("\n".join(lot_lines))
    print(f"[generate-book] Gerado: livro/lot.qmd ({len(tabelas)} tabelas)")


def main():
    copy_shared_files()

    figuras, tabelas = extract_floats()
    generate_lof_lot(figuras, tabelas)

    posts = []
    for content_file in sorted(POSTS_DIR.rglob("_content.qmd"), key=lambda p: extract_order(p.parent.name)):
        rel_path = content_file.relative_to(POSTS_DIR)
        post_name = content_file.parent.name

        index_file = content_file.parent / "index.qmd"
        meta = read_yaml_frontmatter(index_file)

        title = meta.get("title", post_name.replace("_", " ").title())
        categories = []
        if "categories" in meta:
            cats = meta["categories"]
            cats = cats.strip("[]")
            categories = [c.strip().strip('"').strip("'") for c in cats.split(",")]

        include_path = f"../posts/{rel_path}"
        part = classify_part(categories)

        posts.append({
            "file": f"{post_name}.qmd",
            "title": title,
            "include_path": include_path,
            "part": part,
            "order": extract_order(post_name),
        })

    fixed_files = {"index.qmd", "referencias.qmd", "glossario.qmd", "lof.qmd", "lot.qmd", "_quarto.yml"}
    for f in LIVRO_DIR.glob("*.qmd"):
        if f.name not in fixed_files:
            f.unlink()

    for post in posts:
        qmd_path = LIVRO_DIR / post["file"]
        content = f"---\ntitle: \"{post['title']}\"\n---\n\n{{{{< include {post['include_path']} >}}}}\n"
        with open(qmd_path, "w", encoding="utf-8") as f:
            f.write(content)

    chapters = ["index.qmd"]
    if figuras:
        chapters.append("lof.qmd")
    if tabelas:
        chapters.append("lot.qmd")

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
  subtitle: "Programacao Orientada a Objetos"
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
    toc-title: "Sumario"
    # lof e lot desabilitados — bug do Quarto+Typst (#14081)
    # Gerados manualmente pelo generate-book.py
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

    print(f"[generate-book] {len(posts)} capitulos gerados no livro/")
    for p in posts:
        part_info = f" (part: {p['part']})" if p["part"] else ""
        print(f"  - {p['file']}: {p['title']}{part_info}")


if __name__ == "__main__":
    main()
