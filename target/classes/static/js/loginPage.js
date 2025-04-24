document.addEventListener("DOMContentLoaded", function () {
  const isLoggedIn = document.body.dataset.loggedin === 'true';
  const modalEl = document.getElementById('loginModal');
  const loginModal = bootstrap.Modal.getOrCreateInstance(modalEl);

  // 如果 localStorage 指定要自動開啟登入視窗
  if (localStorage.getItem("autoShowLogin") === "true") {
    loginModal.show();
    localStorage.removeItem("autoShowLogin");
  }

  // 點擊需要登入的連結
  document.querySelectorAll(".memberLink").forEach(function (el) {
    el.addEventListener("click", function (e) {
      if (!isLoggedIn) {
        e.preventDefault();
        Swal.fire({
          title: "尚未登入",
          text: "請先登入會員",
          icon: "warning",
          confirmButtonText: "前往登入"
        }).then(() => {
          loginModal.show();
        });
      }
    });
  });

  // 登入表單送出
  const loginForm = document.querySelector(".login-form");
  if (loginForm) {
    loginForm.addEventListener("submit", function (e) {
      e.preventDefault();
      $.ajax({
        url: '/member/login',
        type: 'POST',
        data: $(loginForm).serialize(),
        success: function (response) {
          if (response.status === 'success') {
            Swal.fire({
              icon: 'success',
              title: '登入成功',
              html: `歡迎 <b>${response.name}</b>！`,
              confirmButtonText: '前往首頁'
            }).then(() => {
              window.location.href = '/';
            });
          } else {
            Swal.fire({
              icon: 'error',
              title: '登入失敗',
              text: response.message || '請稍後再試'
            });
          }
        },
        error: function () {
          Swal.fire({
            icon: 'error',
            title: '伺服器錯誤',
            text: '請稍後再試或聯絡客服'
          });
        }
      });
    });
  }
});
