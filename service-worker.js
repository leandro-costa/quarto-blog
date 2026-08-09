const CACHE_NAME = "poo-blog-cache-v1";

// Caminhos relativos ao escopo do service worker (funciona tanto em
// localhost quanto publicado em GitHub Pages num subdiretório, ex.:
// https://leandro-costa.github.io/quarto-blog/).
const APP_SHELL = [
  "./",
  "./index.html",
  "./styles.css",
  "./manifest.webmanifest",
  "./icons/icon-192.png",
  "./icons/icon-512.png",
];

self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => cache.addAll(APP_SHELL))
  );
  self.skipWaiting();
});

self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches.keys().then((keys) =>
      Promise.all(
        keys
          .filter((key) => key !== CACHE_NAME)
          .map((key) => caches.delete(key))
      )
    )
  );
  self.clients.claim();
});

// Estratégia: stale-while-revalidate — responde rápido com o que já está
// em cache (funciona offline) e atualiza o cache em segundo plano sempre
// que há conexão, para que as próximas visitas já tragam a versão nova.
self.addEventListener("fetch", (event) => {
  if (event.request.method !== "GET") return;

  // Não interceptar o PDF do livro (arquivo grande, melhor sempre buscar
  // fresco quando online) nem chamadas para outros domínios.
  const url = new URL(event.request.url);
  if (url.origin !== self.location.origin) return;
  if (url.pathname.endsWith(".pdf")) return;

  event.respondWith(
    caches.open(CACHE_NAME).then((cache) =>
      cache.match(event.request).then((cached) => {
        const fetchPromise = fetch(event.request)
          .then((networkResponse) => {
            if (networkResponse && networkResponse.status === 200) {
              cache.put(event.request, networkResponse.clone());
            }
            return networkResponse;
          })
          .catch(() => cached);
        return cached || fetchPromise;
      })
    )
  );
});
