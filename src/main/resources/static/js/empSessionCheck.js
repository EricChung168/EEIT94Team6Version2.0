setInterval(function () {
    $.ajax({
        url: '/employeeApi/session/autoLogout',
        type: 'GET',
        success: function () {
        },
        error: function (xhr) {
            if (xhr.status === 401) {
                Swal.fire({
                    icon: 'warning',
                    title: '登入逾時',
                    text: '您的登入狀態已失效，系統將自動登出',
                    timer: 5000,
                    showConfirmButton: false
                }).then(() => {
                    window.location.href = '/employee/empLogout';
                });
            }
        }
    });
}, 60000); // 每 60 秒檢查一次