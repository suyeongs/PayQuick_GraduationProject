var express = require('express');
var jwt = require('jsonwebtoken');
var bodyParser = require('body-parser');

var db = require('./db');
var app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));
//app.use(bodyParser.urlencoded({extended: false}));
app.use(express.static('./public'));

//app.set('JWT_SECRET', SECRET_KEY) // JWT 인코딩, 디코딩을 위해 필요한 키값

db.connect(function(err) {
    if (err) {
        console.log('unable to connect to MySQL.');
        process.exit(1);
    }
});

app.use('/customers', require('./routes/customers'));
app.use('/goods', require('./routes/goods'));

app.get('/process/login', function(req, res) {
    var paramId = req.param('id');

    console.log('/precess/login 처리, id: '+ paramId);

    res.write("Success");
    res.end();
});


app.get('/', function (req, res) {
  res.send('서버 실행 중...');
});

module.exports = app;

/*
var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});
*/
