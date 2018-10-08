var moment = require('moment');
require('./moment-lunar');

console.log(moment().year(1995).month(2).date(9).lunar());


console.log(moment().year(2001).month(4).date(6).lunar());
console.log(moment().year(2001).month(4).date(6).lunar().isLeanMonth());

// is leap month
console.log(moment().year(2001).month(5).date(4).lunar());
console.log(moment().year(2001).month(5).date(4).lunar().isLeanMonth());

console.log(moment().year(2001).month(4).date(6).lunar().solar());
console.log(moment().year(2001).month(5).date(4).lunar().solar());
console.log(moment().year(2017).month(0).date(1).solar());
