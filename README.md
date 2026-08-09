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

## Estrutura

```
poo-blog/
├── _quarto.yml            # config do site (navbar = Blog/Aulas/Exercícios/Trabalho/Para Entrega)
├── index.qmd               # home / listagem geral
├── about.qmd                # perfil do professor
├── categorias/              # uma página de listing por categoria (equivalente às páginas de categoria do Hope)
│   ├── aulas.qmd
│   ├── exercicios.qmd
│   ├── trabalhos.qmd
│   └── entregas.qmd
├── posts/
│   ├── _metadata.yml        # metadados comuns (engine jupyter + kernel java)
│   ├── 14_heranca/index.qmd
│   └── exercicios/21_Parcial2_generics/index.qmd
├── livro/                   # projeto separado, type: book, format: typst
│   ├── _quarto.yml
│   ├── index.qmd            # prefácio
│   ├── aula-14-heranca.qmd          # {{< include >}} do post real
│   └── exercicio-21-generics.qmd    # {{< include >}} do post real
├── styles.scss               # customização visual (cor #46bd87, igual ao tema original)
└── .github/workflows/publish.yml   # build + deploy (GitHub Pages) com JDK/IJava configurados
```

## Bibliografia (references.bib)

Existem **duas cópias** do arquivo `references.bib`: uma na raiz do projeto
(usada pelo site/blog em HTML) e outra dentro de `livro/` (usada pelo livro
em PDF). Isso é necessário porque o compilador do Typst roda em um sandbox
que não permite acessar arquivos fora da pasta do projeto do livro
(`livro/`) — um caminho como `../references.bib` é bloqueado.

Sempre que adicionar ou editar uma citação, atualize as duas cópias:

```bash
cp references.bib livro/references.bib
```


1. Criar `posts/NN_titulo/index.qmd` com front matter:
   ```yaml
   ---
   title: "Título da aula"
   author: "Leandro Souza"
   date: "AAAA-MM-DD"
   categories: [aula, tema1, tema2]
   ---
   ```
2. Escrever o conteúdo normalmente; usar ```{java}``` para blocos que devem
   ser compilados/executados, ou ```` ```java ```` (sem chaves) para trechos
   apenas ilustrativos que não devem rodar.
3. `quarto preview .` já mostra a aula na listagem automaticamente.
4. Se quiser incluir a aula no livro, criar um `.qmd` em `livro/` com um
   `{{< include >}}` apontando para o post e adicionar em `book.chapters` no
   `livro/_quarto.yml`.

## Publicando

O workflow `.github/workflows/publish.yml` já:
- instala JDK 21 + Jupyter + kernel IJava no runner;
- renderiza o site inteiro (`quarto render .`);
- renderiza o livro em PDF via Typst;
- copia o PDF para dentro do site publicado (`_site/livro-poo.pdf`);
- publica no GitHub Pages.

Basta habilitar GitHub Pages com origem "GitHub Actions" nas configurações
do repositório.
