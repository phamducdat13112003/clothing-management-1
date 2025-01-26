window.onload = function() {
    var notification = document.getElementById("notification");

    if (notification) {
        // Hiển thị thông báo khi nó được thêm vào trang
        notification.style.display = "block";

        // Sau 3 giây, thông báo sẽ mờ dần và biến mất
        setTimeout(function() {
            notification.style.display = "none";
        }, 3000);
    }
};
