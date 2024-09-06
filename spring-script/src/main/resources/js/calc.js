function getURL(url) {
    var now = new Date();
    var year = now.getFullYear();
    var month = now.getMonth() + 1;
    var day = now.getDate();
    var hours = now.getHours();
    var minutes = now.getMinutes();
    var seconds = now.getSeconds();
    var time = year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;

    return url + "?t=" + time;
}