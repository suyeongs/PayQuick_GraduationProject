var mysql = require('mysql');

var pool;

exports.connect = function() {
    pool = mysql.createPool({
        connectionLimit: 100,
        host: "[ DB host IP ]",
        user: "[ DB username ]",
        database: "payquick_db",
        password: "[ DB password ]",
        port: 3306,
        dateStrings: "date",  // DB DATETIME 형변환
        multipleStatements: true  // 다중쿼리 허용
    });
}

exports.get = function() {
  return pool;
}
