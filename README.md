# Blog de Aulas de POO — Quarto + Typst + Java compilado em build time

Estrutura equivalente ao site atual (VuePress + Theme Hope), migrada para
Quarto, com duas melhorias:

1. Blocos de código Java em ```{java}``` são **compilados e executados de
   verdade** no momento do `quarto render`, via kernel Jupyter **IJava**
   (JShell por baixo dos panos). Se o código não compilar, o build quebra.
2. O mesmo conteúdo das aulas alimenta automaticamente um **livro em PDF**,
   gerado com **Typst** — usando **Quarto Project Profiles**, de forma que
   site e livro compartilham a mesma raiz de projeto e o mesmo
   `references.bib` (arquivo único, sem duplicação).

## Instalação local

```bash
# 1. Quarto
# https://quarto.org/docs/get-started/

# 2. JDK >= 9 (recomendado 21)
sdk install java 21-tem   # ou via apt/brew/instalador oficial

# 3. Jupyter + kernel IJava
pip install jupyter
curl -L https://github.com/SpencerPark/IJava/releases/latest/download/ijava-1.3.0.zip -o ijava.zip
unzip ijava.zip -d ijava
python3 ijava/install.py --user
```

## Rodando localmente

```bash
# Site (blog) — profile padrão
quarto preview .

# Livro em PDF (Typst) — profile "book"
quarto render --profile book --to typst
```

O PDF gerado fica em `_book/`.

## Como funcionam os profiles

Este projeto usa um único `_quarto.yml` (configuração do site/blog) mais um
`_quarto-book.yml` que sobrescreve apenas o necessário para gerar o livro:
`project.type` vira `book`, e o formato de saída vira `typst`. Como os dois
compartilham a mesma raiz de projeto, não existe mais um subprojeto
isolado — e por isso `references.bib` existe em um único lugar, sem
sandbox separado do Typst para se preocupar.

```bash
quarto render --profile book   # usa _quarto.yml + _quarto-book.yml combinados
```

## Estrutura

```
poo-blog/
├── _quarto.yml               # config do site (navbar = Blog/Aulas/Exercícios/Trabalho/Para Entrega)
├── _quarto-book.yml           # profile do livro (project.type: book, format: typst)
├── references.bib             # bibliografia única, compartilhada por site e livro
├── index.qmd                   # home / listagem geral
├── about.qmd                    # perfil do professor
├── categorias/                  # uma página de listing por categoria (equivalente às páginas de categoria do Hope)
│   ├── aulas.qmd
│   ├── exercicios.qmd
│   ├── trabalhos.qmd
│   └── entregas.qmd
├── posts/
│   ├── _metadata.yml            # metadados comuns (engine jupyter + kernel java)
│   ├── 14_heranca/
│   │   ├── index.qmd              # front matter do post (title, date, categories)
│   │   └── _content.qmd            # conteúdo real (sem front matter, reaproveitado no livro)
│   └── exercicio/21_Parcial2_generics/
│       ├── index.qmd
│       └── _content.qmd
├── capitulos/                   # wrappers só para o livro (título + include do _content.qmd)
│   ├── prefacio.qmd
│   ├── aula-14-heranca.qmd
│   ├── exercicio-21-generics.qmd
│   └── referencias.qmd
├── styles.scss                   # customização visual (cor #46bd87, igual ao tema original)
└── .github/workflows/publish.yml     # build + deploy (GitHub Pages) com JDK/IJava configurados
```

A pasta `capitulos/` não é renderizada como parte do site (excluída via
`project.render` no `_quarto.yml`) — ela existe só para dar um título de
capítulo a cada aula quando incluída no livro.

## Adicionando uma nova aula

1. Criar `posts/NN_titulo/index.qmd` com front matter:
   ```yaml
   ---
   title: "Título da aula"
   author: "Leandro Souza"
   date: "AAAA-MM-DD"
   categories: [aula, tema1, tema2]
   ---

   {{< include _content.qmd >}}
   ```
2. Criar `posts/NN_titulo/_content.qmd` com o conteúdo real (sem front
   matter). Usar ```{java}``` para blocos que devem ser compilados/
   executados, ou ```` ```java ```` (sem chaves) para trechos apenas
   ilustrativos que não devem rodar.
3. `quarto preview .` já mostra a aula na listagem automaticamente.
4. Se quiser incluir a aula no livro, criar um `.qmd` em `capitulos/` com
   um `{{< include >}}` apontando para o `_content.qmd` e adicionar em
   `book.chapters` no `_quarto-book.yml`.

## Publicando

O workflow `.github/workflows/publish.yml` já:
- instala JDK 21 + Jupyter + kernel IJava no runner;
- renderiza o site inteiro (`quarto render .`);
- renderiza o livro em PDF via Typst (`quarto render --profile book --to typst`);
- copia o PDF para dentro do site publicado (`_site/livro-poo.pdf`);
- publica no GitHub Pages.

Basta habilitar GitHub Pages com origem "GitHub Actions" nas configurações
do repositório.
