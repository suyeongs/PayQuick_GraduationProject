var jwt = require('jwt-simple');
var express = require('express');
var db = require('../db');
var router = express.Router();


/* 상품 검색 (Client App: 상단 바코드 아이콘->상품 구매) */
router.post('/search', function (req, res) {
    var code_id = req.body.code_id;
    var goods_img;
    var goods_name;
    var color;
    var size;
    var price;

    var sql = 'SELECT * FROM stock WHERE code_id = ?';

    db.get().query(sql, code_id, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다(S)';

        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 204;
                message = '존재하지 않는 상품입니다.';
            } else {
                resultCode = 200;
                goods_img = result[0].goods_img;
                goods_name = result[0].goods_name;
                color = result[0].color;
                size = result[0].size;
                price = result[0].price;

                message = '상품바코드 : ' + result[0].code_id;
                console.log(req.body);
            }
        }

        res.json({
            'code': resultCode,
            'message': message,
            'code_id': code_id,
            'goods_img' : goods_img,
            'goods_name' : goods_name,
            'color' : color,
            'size' : size,
            'price' : price
        });
    })
});

/* 상품 검색 (Client App: 상단 바코드 아이콘->재고 확인) */
router.post('/stock', function (req, res) {
    var code_id = req.body.code_id;
    var goods_name;
    var goods_img = new Array();
    var color = new Array();
    var size = new Array();
    //var stock = 1;
    var stock = new Array();

    //var sql = 'SELECT * FROM stock WHERE goods_name = (SELECT goods_name FROM stock WHERE code_id = ?)';
    var sql = 'SELECT *, COUNT(id) AS stock FROM stock WHERE goods_name = (SELECT goods_name FROM stock WHERE code_id = ?) GROUP BY id';

    db.get().query(sql, code_id, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다(S)';

        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 204;
                message = '존재하지 않는 상품입니다.';
            } else {
                resultCode = 200;
                console.log("행 수 : " + result.length);

                goods_name = result[0].goods_name;
                for (var i = 0; i < result.length; i++) {
                   goods_img[i] = result[i].goods_img;
                   color[i] = result[i].color;
                   size[i] = result[i].size;
                   stock[i] = result[i].stock;
                }
                console.log(req.body);
                message = "재고 검색";
            }
        }

        console.log({
            'code': resultCode,
            'message': message,
            'goods_name' : goods_name,
            'goods_img' : goods_img,
            'color' : color,
            'size' : size,
            'stock' : stock
        });
        res.json({
            'code': resultCode,
            'message': message,
            'goods_name' : goods_name,
            'goods_img' : goods_img,
            'color' : color,
            'size' : size,
            'stock' : stock
        });
    })
});

/* 장바구니 항목 삭제 (Client App: 상단 장바구니 아이콘->항목 터치->확인), 장바구니 보기 (Client App: 상단 장바구니 아이콘), 장바구니 담기 (Client App: 상단 바코드 아이콘->상품 구매->장바구니 담기) */
router.post('/cart', function (req, res) {
    var token = req.body.token;
    var cus_id;
    var own_id;
    //console.log(req.body.code_id);
    var code_id = req.body.code_id;
    var del = req.body.del;
    var params;

    // 토큰에서 cus_id 추출
    var secret = 'mysecret';
    var decoded = jwt.decode(token, secret);
    cus_id = decoded.id;
    console.log(decoded);
    console.log('cus_id : ' + cus_id);

    if (del == true) {  // Android CartData 생성자 파라미터가 3개일 때 (장바구니 항목 삭제)
        var sum = 0;
        var goods_name = new Array();
        var goods_img = new Array();
        var color = new Array();
        var size = new Array();
        var price = new Array();

        params = [code_id, cus_id];

        var sql = 'DELETE FROM cart WHERE code_id = ? AND cus_id = ?;';

        var code_id = new Array();
        db.get().query(sql, params, function (err, result) {
            var resultCode = 404;
            var message = '에러가 발생했습니다(S)';

            if (err) {
                console.log(err);

            } else {
                if (result.length === 0) {
                    resultCode = 404;
                    message = '항목을 찾을 수 없습니다.';
                } else {
                    resultCode = 200;
                    message = "삭제되었습니다.";

                    console.log("행 수 : " + result.length);

                    for (var i = 0; i < result.length; i++) {
                        goods_name[i] = result[i].goods_name;
                        goods_img[i] = result[i].goods_img;
                        color[i] = result[i].color;
                        size[i] = result[i].size;
                        price[i] = result[i].price;
                        code_id[i] = result[i].code_id;

                        sum += result[i].price;
                    }
                    console.log(req.body);
                }

            }

            console.log({
                'code': resultCode,
                'message': message,
                'goods_name' : goods_name,
                'goods_img' : goods_img,
                'color' : color,
                'size' : size,
                'price' : price,
                'len' : result.length,
                'sum' : sum
            });
            res.json({
                'code': resultCode,
                'message': message,
                'goods_name' : goods_name,
                'goods_img' : goods_img,
                'color' : color,
                'size' : size,
                'price' : price,
                'len' : result.length,
                'sum' : sum,
                'code_id' : code_id
            });
        });

    } else if (code_id === undefined) {  // Android CartData 생성자 파라미터가 1개일 때 (장바구니 보기)
        var sum = 0;
        var goods_name = new Array();
        var goods_img = new Array();
        var color = new Array();
        var size = new Array();
        var price = new Array();
        var code_id = new Array();

        var sql = 'SELECT c.cus_id, c.code_id, s.goods_name, s.size, s.color, s.price, s.goods_img FROM cart c, stock s WHERE c.cus_id = ? AND c.code_id = s.code_id';

        db.get().query(sql, cus_id, function (err, result) {
            var resultCode = 404;
            var message ='에러가 발생했습니다(S)';

            if (err) {
                console.log(err);
            } else {
                if (result.length === 0) {
                    resultCode = 204;
                    message = '담긴 상품이 없습니다.';
                } else {
                    resultCode = 200;
                    console.log("행 수 : " + result.length);

                    for (var i = 0; i < result.length; i++) {
                        goods_name[i] = result[i].goods_name;
                        goods_img[i] = result[i].goods_img;
                        color[i] = result[i].color;
                        size[i] = result[i].size;
                        price[i] = result[i].price;
                        code_id[i] = result[i].code_id;

                        sum += result[i].price;
                    }
                    console.log(req.body);
                    message = "장바구니 확인";
                }
            }

            console.log({
                'code': resultCode,
                'message': message,
                'goods_name' : goods_name,
                'goods_img' : goods_img,
                'color' : color,
                'size' : size,
                'price' : price,
                'len' : result.length,
                'sum' : sum
            });
            res.json({
                'code': resultCode,
                'message': message,
                'goods_name' : goods_name,
                'goods_img' : goods_img,
                'color' : color,
                'size' : size,
                'price' : price,
                'len' : result.length,
                'sum' : sum,
                'code_id' : code_id
            });
        });
    } else {  // Android CartData 생성자 파라미터가 2개일 때 (장바구니 담기)

      //code_id = req.body.code_id;
        params = [cus_id, code_id];

        var sql = 'INSERT INTO cart (cus_id, code_id) VALUES (?, ?)';
        db.get().query(sql, params, function (err, result) {
            var resultCode = 404;
            var message = '에러가 발생했습니다(S)';

            if (err) {
                if (err.message.includes('ER_DUP_ENTRY')) {  // 이미 담은 상품인 경우
                    resultCode = 204;
                    message = '이미 담긴 상품입니다.'
                    console.log(cus_id + ' 이미 장바구니 담음');

                    res.json({
                        'code': resultCode,
                        'message': message
                    });
                } else {
                    console.log(err);
                }
            } else {
                resultCode = 200;
                message = '장바구니에 담았습니다.';
                console.log(req.body);

                res.json({
                    'code': resultCode,
                    'message': message
                });
            }
        });
    }
});

/* 장바구니 결제 (Client App: 상단 장바구니 아이콘->결제하기), 바로 결제 (Client App: 상단 바코드 아이콘->상품 구매->바로 결제하기) */
router.post('/pay', function (req, res) {
    var pay_time = req.body.pay_time;
    var code_id = req.body.code_id;
    var cus_id;
    var token = req.body.token;
    var params;

    // 토큰에서 cus_id 추출
    var secret = 'mysecret';
    var decoded = jwt.decode(token, secret);
    cus_id = decoded.id;
    console.log(decoded);
    console.log('cus_id : ' + cus_id);

    if (code_id === undefined) {  // Android PayData 생성자 파라미터가 2개일 때 (장바구니 결제)
        //var sql = 'SELECT * FROM sold WHERE code_id IN (SELECT code_id FROM cart WHERE cus_id = ?)'; // 조건문으로 바꾸기, 품절 처리
        params = [cus_id, cus_id, cus_id, pay_time, cus_id, cus_id];

        var sql = 'INSERT INTO sold SELECT * FROM stock WHERE code_id IN (SELECT code_id FROM cart WHERE cus_id = ?);'
                + 'INSERT INTO payinfo (cus_id, own_id, code_id) SELECT c.cus_id, s.own_id, c.code_id FROM stock s, cart c WHERE c.code_id = s.code_id AND c.cus_id = ?;'
                + 'DELETE FROM stock WHERE code_id IN (SELECT code_id FROM cart WHERE cus_id = ?);'
                + 'UPDATE payinfo SET pay_time = ? WHERE code_id IN (SELECT code_id FROM cart WHERE cus_id = ?);'  // 결제 시간을 입력한다
                + 'DELETE FROM cart WHERE cus_id = ?;'  // 해당 회원의 장바구니에서 구매 상품을 삭제한다
                + 'DELETE FROM cart WHERE code_id IN (SELECT s.code_id FROM sold s);';  // 판매된 상품은 모든 회원의 장바구니에서 삭제한다

        //var sql = 'INSERT INTO sold SELECT * FROM stock WHERE code_id IN (SELECT code_id FROM cart WHERE cus_id = ?);'
            // 'INSERT INTO payinfo (cus_id, own_id, code_id) SELECT c.cus_id, s.own_id, c.code_id FROM stock s, cart c WHERE c.code_id = s.code_id AND c.cus_id = ?;';
         // 'DELETE FROM stock WHERE code_id IN (SELECT code_id FROM cart WHERE cus_id = ?);';
        //  var sql  = 'UPDATE payinfo SET pay_time = ? WHERE code_id IN (SELECT code_id FROM cart WHERE cus_id = ?);';
        //+ 'DELETE FROM cart WHERE cus_id = ?;';


        db.get().query(sql, params, function (err, result) {
            var resultCode = 404;
            var message = '에러가 발생했습니다(S)';

            if (err) {
                console.log(err);
            } else {
                resultCode = 200;
                message = '결제가 완료되었습니다.\n구매 내역은 MyPage > Orders 에서 확인할 수 있습니다.';
                //message = '결제가 완료되었습니다. 구매 내역은 MyPage > Orders 에서 확인할 수 있습니다.';
            }

            console.log({
                'code': resultCode,
                'message': message
            });
            res.json({
                'code': resultCode,
                'message': message
            });
        });
    } else {  // Android PayData 생성자 파라미터가 3개일 때 (바로 결제)
        params = [code_id, pay_time, cus_id, code_id, code_id, code_id];

        var sql = 'INSERT INTO sold SELECT * FROM stock WHERE code_id = ?;'
                + 'INSERT INTO payinfo (pay_time, cus_id, own_id, code_id) VALUES (?, ?, (SELECT s.own_id FROM sold s WHERE s.code_id = ?), ?);'
                + 'DELETE FROM stock WHERE code_id = ?;'
                + 'DELETE FROM cart WHERE code_id IN (SELECT s.code_id FROM sold s);';  // 판매된 상품은 모든 회원의 장바구니에서 삭제한다

        db.get().query(sql, params, function (err, result) {
            var resultCode = 404;
            var message = '에러가 발생했습니다(S)';

            if (err) {
                console.log(err);
            } else {
                resultCode = 200;
                message = '결제가 완료되었습니다.\n구매 내역은 MyPage > Orders 에서 확인할 수 있습니다.';
                //message = '구매 내역은 MyPage > Orders 에서 확인할 수 있습니다.';
            }

            console.log({
                'code': resultCode,
                'message': message
            });
            res.json({
                'code': resultCode,
                'message': message
            });
        });
    }
});

/* 구매 내역 조회 (Client App: MyPage->Orders), 구매 세부 내역 조회 (Client App: MyPage->Orders->항목 터치) */
router.post('/orders', function (req, res) {
    var token = req.body.token;
    var cus_id;
    var pay_time = req.body.pay_time;
    var sum;

    // 토큰에서 cus_id 추출
    var secret = 'mysecret';
    var decoded = jwt.decode(token, secret);
    cus_id = decoded.id;
    console.log(decoded);
    console.log('cus_id : ' + cus_id);

    if (pay_time == undefined) {  // Android OrdersData 생성자 파라미터가 1개일 때 (구매 내역 조회)
        var goods_name = new Array();
        var goods_img = new Array();
        var pay_time = new Array();
        var price = new Array();
        var count = new Array();

        var sql = 'SELECT p.cus_id, p.pay_time, s.goods_name, s.goods_img, s.price FROM payinfo p, sold s WHERE p.code_id = s.code_id AND cus_id = ? ORDER BY pay_time DESC;';

        db.get().query(sql, cus_id, function (err, result) {
            var resultCode = 404;
            var message = '에러가 발생했습니다(S)';

            if (err) {
                console.log(err);
            } else {
                if (result.length === 0) {
                    resultCode = 204;
                    message = '구매 내역이 없습니다.';
                } else {
                    resultCode = 200;
                    message = '내역 조회 성공';

                    console.log("행 수 : " + result.length);

                    var k = 0;
                    goods_name[0] = result[0].goods_name;
                    goods_img[0] = result[0].goods_img;
                    pay_time[0] = result[0].pay_time;
                    price[0] = result[0].price;
                    count[0] = 1;
                    sum = result[0].price;

                    // 결제 시간을 기준으로 각 컬럼을 그룹화
                    for (var i = 1; i < result.length; i++) {
                        if (pay_time[k] == result[i].pay_time) {
                            price[k] += result[i].price;
                            count[k]++;

                            sum += result[i].price;
                        } else {
                            k++;
                            count[k] = 1;

                            pay_time[k] = result[i].pay_time;
                            goods_name[k] = result[i].goods_name;
                            goods_img[k] = result[i].goods_img;
                            price[k] = result[i].price;

                            sum += result[i].price;
                        }
                    }
                }
            }
            console.log({
                'code': resultCode,
                'message': message,
                'goods_name' : goods_name,
                'goods_img' : goods_img,
                'pay_time' : pay_time,
                'price' : price,
                'count' : count,
                'len' : goods_name.length,
                'sum' : sum
            });
            res.json({
                'code': resultCode,
                'message': message,
                'goods_name' : goods_name,
                'goods_img' : goods_img,
                'pay_time' : pay_time,
                'price' : price,
                'count' : count,
                'len' : goods_name.length,
                'sum' : sum
            });
        });
    } else {  // Android OrdersData 생성자 파라미터가 1개일 때 (구매 세부 내역 조회)
        sum = 0;
        var goods_name = new Array();
        var goods_img = new Array();
        var color = new Array();
        var size = new Array();
        var price = new Array();
        var code_id = new Array();
        var params = [cus_id, pay_time];

        var sql = 'SELECT s.code_id, s.goods_name, s.size, s.color, s.goods_img, s.price FROM payinfo p, sold s WHERE p.code_id = s.code_id AND cus_id = ? AND pay_time = ?;';

        db.get().query(sql, params, function (err, result) {
            var resultCode = 404;
            var message = '에러가 발생했습니다(S)';

            if (err) {
                console.log(err);
            } else {
                resultCode = 200;
                message = '세부 내역 조회 성공';

                console.log("행 수 : " + result.length);

                for (var i = 0; i < result.length; i++) {
                    goods_name[i] = result[i].goods_name;
                    goods_img[i] = result[i].goods_img;
                    color[i] = result[i].color;
                    size[i] = result[i].size;
                    price[i] = result[i].price;
                    code_id[i] = result[i].code_id;

                    sum += result[i].price;
                }

                console.log({
                    'code': resultCode,
                    'message': message,
                    'goods_name' : goods_name,
                    'goods_img' : goods_img,
                    'color' : color,
                    'size' : size,
                    'price' : price,
                    'len' : result.length,
                    'sum' : sum
                });
                res.json({
                    'code': resultCode,
                    'message': message,
                    'goods_name' : goods_name,
                    'goods_img' : goods_img,
                    'color' : color,
                    'size' : size,
                    'price' : price,
                    'len' : result.length,
                    'sum' : sum
                });
            }
        });
    }
});

/* 신제품 랜덤 노출 (Client App: Home) */
router.post('/new', function (req, res) {
    var goods_name = req.body.goods_name;
    var goods_img = new Array();
    var color = new Array();
    var size = new Array();
    var price = new Array();

    //var own_tmp = 'admin';
    var size_tmp = 'F';

    //var sql = 'SELECT * FROM stock WHERE goods_name = ? GROUP BY id';
    //var sql = 'SELECT * FROM stock WHERE own_id <> ? ORDER BY RAND() LIMIT 6';
    var sql = 'SELECT * FROM (SELECT * FROM new WHERE size = ? ORDER BY rand() LIMIT 10) T1 ORDER BY goods_name DESC';

    db.get().query(sql, size_tmp, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다(S)';

        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 204;
                message = '존재하지 않는 상품입니다.';
            } else {
                resultCode = 200;
                console.log("행 수 : " + result.length);

                goods_name = new Array();

                for (var i = 0; i < result.length; i++) {
                   goods_img[i] = result[i].goods_img;
                   goods_name[i] = result[i].goods_name;
                   color[i] = result[i].color;
                   size[i] = result[i].size;
                   price[i] = result[i].price;
                }
                message = "신제품 표시";
                console.log(req.body);
            }
        }

        console.log({
            'code': resultCode,
            'message': message,
            'goods_name' : goods_name,
            'goods_img' : goods_img,
            'color' : color,
            'size' : size,
            'price' : price
        });
        res.json({
            'code': resultCode,
            'message': message,
            'goods_name' : goods_name,
            'goods_img' : goods_img,
            'color' : color,
            'size' : size,
            'price' : price
        });
    });
});

/* 구매한 상품 모두 환불 처리 - 테스트시 DB 복구하기 위함 (Client App: 회원가입 화면->PayQuick 로고 터치) */
router.post('/restore', function (req, res) {
    var restore = req.body.restore;

    var sql = 'DELETE FROM payinfo WHERE code_id IN (SELECT code_id FROM tmp_stock);'
            + 'INSERT INTO stock SELECT * FROM sold WHERE code_id IN (SELECT code_id FROM tmp_stock);'
            + 'DELETE FROM sold WHERE code_id IN (SELECT code_id FROM tmp_stock);';

    db.get().query(sql, restore, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다(S)';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = '구매 관련 DB 복구 완료';
        }
        console.log({
            'code': resultCode,
            'message': message,
        });
        res.json({
            'code': resultCode,
            'message': message,
        });
    });
});

module.exports = router;
