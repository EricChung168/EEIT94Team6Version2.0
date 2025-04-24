async function loadCarousel() {
  try {
    const res = await fetch("/api/news/ads");
    const ads = await res.json();

    const container = document.getElementById("carouselContent");
    container.innerHTML = "";

    ads.forEach((ad, index) => {
      const imageUrl = ad.imageUrl || "/images/default.jpg";
      container.innerHTML += `
        <div class="carousel-item ${index === 0 ? "active" : ""}">
          <a href="/news-detail1?id=${ad.id}">
            <img src="${imageUrl}" class="d-block w-100"
              style="max-height: 500px; object-fit: cover;" alt="公告圖片${index + 1}">
          </a>
        </div>
      `;
    });
  } catch (err) {
    console.error("輪播載入失敗", err);
  }
}

window.onload = loadCarousel;