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

## PWA (Progressive Web App)

O site pode ser instalado como app (celular ou desktop) e continua
funcionando offline para páginas já visitadas. A implementação usa:

- `manifest.webmanifest` — nome, ícones, cor do tema, modo `standalone`.
- `service-worker.js` — cacheia o "app shell" no primeiro acesso e usa a
  estratégia *stale-while-revalidate* (responde do cache instantaneamente e
  atualiza em segundo plano quando há conexão). O PDF do livro é
  deliberadamente excluído do cache automático (arquivo grande, melhor
  buscar sempre a versão mais recente quando online).
- `pwa-head.html` — incluído via `include-in-header` no formato HTML: link
  do manifest, `theme-color`, ícones para tela inicial (Android/iOS) e o
  script que registra o service worker.
- `icons/` — ícones gerados a partir de `logo.svg` (192×192, 512×512,
  apple-touch-icon, favicons).

Esses arquivos são estáticos (não são `.qmd`), então precisam estar
listados em `project.resources` no `_quarto.yml` para o Quarto copiá-los
para `_site/` no build — já configurado.

**Testando localmente**: PWAs exigem HTTPS ou `localhost` para o service
worker funcionar (não funciona abrindo o HTML direto do disco, `file://`).
Use `quarto preview .` normalmente. Em produção, o GitHub Pages já serve
tudo em HTTPS.

**Atualizando o cache**: o `CACHE_NAME` usa um placeholder
(`__COMMIT_SHA__`) que o workflow do GitHub Actions substitui
automaticamente pelo hash curto do commit a cada deploy — ou seja, todo
push que altera o site já força os navegadores a descartar o cache antigo
e buscar a versão nova, sem precisar editar nada manualmente. Rodando
`quarto preview .` localmente, o placeholder fica literal no arquivo (sem
efeito prático além do nome do cache).



O profile `_quarto-book.yml` aplica formatação inspirada na NBR 14724
(margens 3cm/3cm/2cm/2cm, fonte 12pt, recuo de parágrafo de 1,25cm,
entrelinhas ~1,5, sumário/referências/citações com links clicáveis, e
uma bibliografia formatada com o estilo `abnt.csl`), além de um capítulo de
**Glossário** e listas de figuras/tabelas (`lof`/`lot`).

Dois pontos de atenção:

- **`lof`/`lot` em Typst são um recurso recente do Quarto** (chegou junto
  com o suporte a livros em Typst). Se a sua versão do Quarto for anterior
  ou essas listas não aparecerem no PDF, atualize o Quarto
  (`quarto --version` deve ser 1.9 ou mais recente).
- O arquivo `abnt.csl` foi baixado do repositório oficial do
  [Citation Style Language](https://github.com/citation-style-language/styles)
  e já está no projeto — não precisa reinstalar nada.

Essa formatação atende bem a **material didático/apostila**. Se este PDF
for usado como TCC, dissertação ou outro documento formal que exige
conformidade estrita com a ABNT (capa padronizada, folha de rosto, ficha
catalográfica, resumo em português/inglês etc.), essas seções adicionais
precisam ser criadas à parte — não são geradas automaticamente a partir do
conteúdo do blog.



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
