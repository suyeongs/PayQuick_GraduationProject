var jwt = require('jwt-simple');
var express = require('express');
var db = require('../db');
var router = express.Router();


/* 회원정보 조회 (Client App: MyPage->Account), 로그인 */
router.post('/login', function (req, res) {
    var token = req.body.token;
    var cus_id = req.body.cus_id;
    var cus_pw = req.body.cus_pw;
    var cus_phone;
    var cus_name;

    if (cus_pw === undefined) {  // Android LoginData 생성자 파라미터가 1개일 때 (회원정보 조회)
        // 토큰에서 cus_id 추출
        var secret = 'mysecret';
        var decoded = jwt.decode(token, secret);
        cus_id = decoded.id;
        console.log(decoded);
        console.log('cus_id : ' + cus_id);

        var sql = 'SELECT * FROM customers WHERE cus_id = ?';

        db.get().query(sql, cus_id, function (err,result) {
            var resultCode = 404;
            var message = '에러가 발생했습니다(S)';

            if (err) {
                console.log(err);
            } else {
                resultCode = 200;
                message = '회원정보 조회 성공';

                cus_pw = result[0].cus_pw;
                cus_phone = result[0].cus_phone;
                cus_name = result[0].cus_name;

                console.log({
                  'code': resultCode,
                  'message': message,
                  'cus_pw' : cus_pw,
                  'cus_id' : cus_id,
                  'cus_phone' : cus_phone,
                  'cus_name' : cus_name
                });
                res.json({
                    'code': resultCode,
                    'message': message,
                    'cus_pw' : cus_pw,
                    'cus_id' : cus_id,
                    'cus_phone' : cus_phone,
                    'cus_name' : cus_name
                });
            }
        });
    } else {  // Android LoginData 생성자 파라미터가 2개일 때 (로그인)
        var sql = 'SELECT * FROM customers WHERE cus_id = ?';

        var body = { };
        var secret = 'mysecret';
        var token;

        db.get().query(sql, cus_id, function (err, result) {
            var resultCode = 404;
            var message = '에러가 발생했습니다(S)';

            if (err) {
                console.log(err);
            } else {
                if (result.length === 0) {
                    resultCode = 204;
                    message = '존재하지 않는 계정입니다.';
                } else if (cus_pw !== result[0].cus_pw) {
                    resultCode = 204;
                    message = '비밀번호가 일치하지 않습니다.';
                } else {
                    resultCode = 200;
                    message = result[0].cus_name + '님 환영합니다!';
                    console.log(req.body);

                    cus_name = result[0].cus_name;
                    console.log('name : ' + cus_name);

                    // 토큰 전송
                    body.id = result[0].cus_id;
                    console.log(body);
                    token = jwt.encode(body, secret);
                    console.log('token : ' + token);
                }
            }
            console.log({
                'code': resultCode,
                'message': message,
                'cus_id': cus_id,
                'cus_name' : cus_name,
                'token' : token
            });
            res.json({
                'code': resultCode,
                'message': message,
                'cus_id': cus_id,
                'cus_name' : cus_name,
                'token' : token
            });
        });
    }
});

/* 회원정보 수정 (Client App: MyPage->Account->수정하기), 회원가입 */
router.post('/register', function (req, res) {
    //console.log(req.body);
    var token = req.body.token;
    var cus_id = req.body.cus_id;
    var cus_pw = req.body.cus_pw;
    var cus_name = req.body.cus_name;
    var cus_phone = req.body.cus_phone;
    let {id, pw, name, phone} = req.body;

    if (cus_name === undefined) {  // Android RegisterData 생성자 파라미터가 3개일 때 (회원정보 수정)
        // 토큰에서 cus_id 추출
        var secret = 'mysecret';
        var decoded = jwt.decode(token, secret);
        cus_id = decoded.id;
        console.log(decoded);
        console.log('cus_id : ' + cus_id);

        var sql = 'UPDATE customers SET cus_pw = ? WHERE cus_id = ?;'
                + 'UPDATE customers SET cus_phone = ? WHERE cus_id = ?;';
        var params = [cus_pw, cus_id, cus_phone, cus_id];

        db.get().query(sql, params, function (err, result) {
            var resultCode = 404;
            var message = '에러가 발생했습니다(S)';

            if (err) {
                console.log(err);
            } else {
                resultCode = 200;
                message = '회원정보가 수정되었습니다.';

                console.log({
                    'code': resultCode,
                    'message': message
                });
                res.json({
                    'code': resultCode,
                    'message': message
                });
            }
        });
    } else {  // Android RegisterData 생성자 파라미터가 4개일 때 (회원가입)
        // 삽입을 수행하는 sql문.
        var sql = 'INSERT INTO customers (cus_id, cus_pw, cus_name, cus_phone) VALUES (?, ?, ?, ?)';
        var params = [cus_id, cus_pw, cus_name, cus_phone];

        // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
        db.get().query(sql, params, function (err, result) {
            var resultCode = 404;
            var message = '에러가 발생했습니다(S)';

            if (err) {
                console.log(err);
            } else {
                resultCode = 200;
                message = '회원가입이 완료되었습니다.';
                console.log(req.body);

                console.log({
                    'code': resultCode,
                    'message': message
                });
                res.json({
                    'code': resultCode,
                    'message': message
                });
            }
        });
    }
});

module.exports = router;
