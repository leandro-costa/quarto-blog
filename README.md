# Blog de Aulas de POO — Quarto + Typst + Java compilado em build time

Estrutura equivalente ao site atual (VuePress + Theme Hope), migrada para
Quarto, com duas melhorias:

1. Blocos de código Java em ```{java}``` são **compilados e executados de
   verdade** no momento do `quarto render`, via kernel Jupyter **IJava**
   (JShell por baixo dos panos). Se o código não compilar, o build quebra.
2. O mesmo conteúdo das aulas alimenta automaticamente um **livro em PDF**
   (`livro/`), gerado com **Typst**.

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
# Site (blog)
quarto preview .

# Livro em PDF (Typst)
quarto render livro --to typst
```

O PDF gerado fica em `livro/_book/`.

## Por que `livro/` é um subprojeto separado (e não um profile)

Já tentamos usar Quarto Project Profiles (um único `_quarto.yml` alternando
`project.type` entre `website` e `book`) para ter um `references.bib`
verdadeiramente único. Esbarramos numa limitação documentada do próprio
Quarto: **todo projeto `book` exige um `index.qmd` na raiz do projeto**
(é a home page do livro, obrigatória mesmo para saída só em PDF, porque
Quarto livros também geram uma versão HTML). Não existe uma chave de
configuração (`book: index:` ou similar) para apontar isso para outro
arquivo. Como a raiz do site já tem seu próprio `index.qmd` (a home do
blog, com conteúdo totalmente diferente), os dois projetos não podem
compartilhar a mesma raiz — por isso `livro/` voltou a ser um subprojeto
com seu próprio `_quarto.yml` e seu próprio `index.qmd`.

## Bibliografia e estilo de citação únicos (via symlink)

Para não duplicar fisicamente os arquivos `references.bib` e `abnt.csl`,
`livro/references.bib` e `livro/abnt.csl` são **links simbólicos** para os
arquivos na raiz do projeto:

```bash
livro/references.bib -> ../references.bib
livro/abnt.csl        -> ../abnt.csl
```

Assim existe uma única fonte de verdade, editada em um único lugar, mesmo
com dois projetos Quarto distintos. Isso funciona porque o Typst só
recusa caminhos que **escapem** da pasta do projeto quando o caminho é
resolvido literalmente (ex.: `../references.bib` dentro do YAML); como o
symlink já fica fisicamente dentro de `livro/`, o Typst enxerga um arquivo
local normal. Se o `git clone` do repositório for feito em um sistema que
não preserva links simbólicos (raro, mas acontece em alguns ambientes
Windows sem privilégio de administrador), o symlink pode virar um arquivo
de texto com o caminho em vez do conteúdo real — nesse caso, a solução de
contingência é copiar o conteúdo manualmente:
```bash
cp references.bib livro/references.bib
cp abnt.csl livro/abnt.csl
```

## Estrutura

```
poo-blog/
├── _quarto.yml               # config do site (navbar = Blog/Aulas/Exercícios/Trabalho/Para Entrega)
├── references.bib             # bibliografia (fonte única — livro/references.bib é um symlink pra cá)
├── abnt.csl                    # estilo de citação ABNT (fonte única — livro/abnt.csl é um symlink pra cá)
├── index.qmd                    # home / listagem geral do blog
├── about.qmd                     # perfil do professor
├── categorias/                   # uma página de listing por categoria (equivalente às páginas de categoria do Hope)
│   ├── aulas.qmd
│   ├── exercicios.qmd
│   ├── trabalhos.qmd
│   └── entregas.qmd
├── posts/
│   ├── _metadata.yml             # metadados comuns (engine jupyter + kernel java)
│   ├── 14_heranca/
│   │   ├── index.qmd               # front matter do post (title, date, categories)
│   │   └── _content.qmd             # conteúdo real (sem front matter, reaproveitado no livro)
│   └── exercicio/21_Parcial2_generics/
│       ├── index.qmd
│       └── _content.qmd
├── livro/                        # subprojeto do livro (project.type: book, format: typst)
│   ├── _quarto.yml
│   ├── index.qmd                   # prefácio — home page obrigatória do livro
│   ├── aula-14-heranca.qmd           # título + {{< include >}} do _content.qmd real
│   ├── exercicio-21-generics.qmd
│   ├── referencias.qmd
│   ├── glossario.qmd
│   ├── references.bib               # symlink -> ../references.bib
│   └── abnt.csl                      # symlink -> ../abnt.csl
├── styles.scss                    # customização visual (cor #46bd87, igual ao tema original)
└── .github/workflows/publish.yml      # build + deploy (GitHub Pages) com JDK/IJava configurados
```

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

## Formatação ABNT do PDF

O `livro/_quarto.yml` aplica formatação inspirada na NBR 14724 (margens
3cm/3cm/2cm/2cm, fonte 12pt, recuo de parágrafo de 1,25cm, entrelinhas
~1,5, sumário/referências/citações com links clicáveis, e bibliografia
formatada com o estilo `abnt.csl`), além de um capítulo de **Glossário** e
listas de figuras/tabelas (`lof`/`lot`).

Dois pontos de atenção:

- **`lof`/`lot` em Typst são um recurso recente do Quarto** (chegou junto
  com o suporte a livros em Typst). Se a sua versão do Quarto for anterior
  ou essas listas não aparecerem no PDF, atualize o Quarto
  (`quarto --version` deve ser 1.9 ou mais recente).
- Quando `format: typst` é usado em um projeto `book`, o Quarto usa
  automaticamente a extensão `orange-book`, que já traz um estilo de
  livro-texto pronto — nosso `include-in-header` sobrescreve pontualmente
  a numeração de títulos e o espaçamento, mas o restante do visual (cores,
  cabeçalhos de capítulo) vem dessa extensão por padrão.

Essa formatação atende bem a **material didático/apostila**. Se este PDF
for usado como TCC, dissertação ou outro documento formal que exige
conformidade estrita com a ABNT (capa padronizada, folha de rosto, ficha
catalográfica, resumo em português/inglês etc.), essas seções adicionais
precisam ser criadas à parte — não são geradas automaticamente a partir do
conteúdo do blog.

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
4. Se quiser incluir a aula no livro, criar um `.qmd` em `livro/` com um
   `{{< include >}}` apontando para o `_content.qmd` e adicionar em
   `book.chapters` no `livro/_quarto.yml`.

## Publicando

O workflow `.github/workflows/publish.yml` já:
- instala JDK 21 + Jupyter + kernel IJava no runner;
- renderiza o site inteiro (`quarto render .`);
- renderiza o livro em PDF via Typst (`quarto render livro --to typst`);
- copia o PDF para dentro do site publicado (`_site/livro-poo.pdf`);
- publica no GitHub Pages.

Basta habilitar GitHub Pages com origem "GitHub Actions" nas configurações
do repositório.
