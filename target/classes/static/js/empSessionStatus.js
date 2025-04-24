// 強制檢查登入狀態，防止快取畫面讓未登入者看到資料
  fetch('/employeeApi/session/checkEmpStatus')
    .then(res => {
      if (!res.ok) {
        // 若未登入就導向登入提示頁
        window.location.href = '/employee/pleaseLogin';
      }
    })
    .catch(() => {
      window.location.href = '/employee/pleaseLogin';
    });