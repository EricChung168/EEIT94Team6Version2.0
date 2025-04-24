window.addEventListener("DOMContentLoaded", () => {
  loadMemberOptions();

  const modal = new bootstrap.Modal(document.getElementById("newsModal"));
  const form = document.getElementById("newsForm");

  let currentPage = 0;
  const pageSize = 5;
  let currentKeyword = "";

  async function loadNews(page = 0) {
    currentPage = page;
    let res;
    if (currentKeyword) {
      res = await fetch(
        `/api/news/search-page?keyword=${encodeURIComponent(
          currentKeyword
        )}&page=${page}&size=${pageSize}&sort=publishDate,desc`
      );
    } else {
      res = await fetch(
        `/api/news?page=${page}&size=${pageSize}&sort=publishDate,desc`
      );
    }

    const data = await res.json();

    const tbody = document.getElementById("newsTableBody");
    tbody.innerHTML = "";

    if (data.content.length === 0) {
      tbody.innerHTML = `<tr><td colspan="5" class="text-center text-muted">查無資料</td></tr>`;
      renderPagination(0); // 沒有資料也要清空分頁按鈕
      return;
    }

    data.content.forEach((news) => {
      tbody.innerHTML += `
        <tr>
          <td>${news.id}</td>
          <td><a href="/news-detail1?id=${news.id}" target="_blank">${
        news.title
      }</a></td>
          <td>${news.status}</td>
          <td>${new Date(news.publishDate).toLocaleString()}</td>
          <td>
            <button class="btn btn-sm btn-primary me-1" onclick='editNews(${JSON.stringify(
              news
            )})'>編輯</button>
            <hr>
            <button class="btn btn-sm btn-danger" onclick='deleteNews(${
              news.id
            })'>刪除</button>
          </td>
        </tr>
      `;
    });

    renderPagination(data.totalPages);
  }

  function renderPagination(totalPages) {
    const container = document.getElementById("paginationContainer");
    container.innerHTML = "";

    // 上一頁按鈕
    const prevBtn = document.createElement("button");
    prevBtn.textContent = "«";
    prevBtn.className = "btn btn-sm btn-outline-secondary me-1";
    prevBtn.disabled = currentPage === 0;
    prevBtn.addEventListener("click", () => loadNews(currentPage - 1));
    container.appendChild(prevBtn);

    // 頁碼按鈕
    for (let i = 0; i < totalPages; i++) {
      const btn = document.createElement("button");
      btn.textContent = i + 1;
      btn.className = "btn btn-sm btn-outline-primary me-1";
      if (i === currentPage) btn.classList.add("active");

      btn.addEventListener("click", () => loadNews(i));
      container.appendChild(btn);
    }

    // 下一頁按鈕
    const nextBtn = document.createElement("button");
    nextBtn.textContent = "»";
    nextBtn.className = "btn btn-sm btn-outline-secondary";
    nextBtn.disabled = currentPage === totalPages - 1;
    nextBtn.addEventListener("click", () => loadNews(currentPage + 1));
    container.appendChild(nextBtn);
  }

  function openForm() {
    form.reset();
    document.getElementById("newsId").value = "";
    modal.show();
  }

  function editNews(news) {
    document.getElementById("newsId").value = news.id;
    document.getElementById("title").value = news.title;
    document.getElementById("summary").value = news.summary;
    document.getElementById("isAd").checked = !!news.isAd;
    document.getElementById("content").value = news.content;
    document.getElementById("imageUrl").value = news.imageUrl;
    document.getElementById("type").value = news.type;
    document.getElementById("status").value = news.status;
    const preview = document.getElementById("previewImage");
    if (preview && news.imageUrl) {
      preview.src = news.imageUrl;
    }
    modal.show();
  }

  document
    .getElementById("imageFile")
    .addEventListener("change", async function () {
      const file = this.files[0];
      if (!file) return;

      const formData = new FormData();
      formData.append("file", file);

      const res = await fetch("/api/news/upload", {
        method: "POST",
        body: formData,
      });

      if (!res.ok) {
        alert("圖片上傳失敗！");
        return;
      }

      const result = await res.json();
      document.getElementById("imageUrl").value = result.url;
      const preview = document.getElementById("previewImage");
      if (preview) {
        preview.src = result.url;
      }
    });

  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    const id = document.getElementById("newsId").value;
    const payload = {
      id: id || undefined,
      title: form.title.value,
      summary: form.summary.value,
      content: form.content.value,
      imageUrl: form.imageUrl.value,
      status: form.status.value,
      isAd: document.getElementById("isAd").checked,
      publishDate: new Date().toISOString(),
    };

    const method = id ? "PUT" : "POST";
    const url = "/api/news" + (id ? "/" + id : "");

    const res = await fetch(url, {
      method: method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    if (res.ok) {
      modal.hide();
      loadNews();
      alert("儲存成功！");
    } else {
      alert("儲存失敗，請稍後再試！");
    }
  });

  async function deleteNews(id) {
    if (confirm("確定要刪除這則新聞嗎？")) {
      const res = await fetch(`/api/news/${id}`, { method: "DELETE" });
      if (res.ok) loadNews();
    }
  }

  // 讓新增按鈕可以用
  window.openForm = openForm;
  window.editNews = editNews;
  window.deleteNews = deleteNews;
  document
    .getElementById("adminSearchForm")
    .addEventListener("submit", async function (e) {
      e.preventDefault();
      currentKeyword = document.getElementById("adminSearchInput").value.trim();
      loadNews(0); // 正確觸發搜尋+分頁渲染
    });

  loadNews();
});
