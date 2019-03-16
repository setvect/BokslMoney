import moment from "moment";
import "./module/moment-lunar";

var CalendarUtil = {};

// 해당 년 주요 기념일 반환
CalendarUtil.getAnniversary = function(year){

  const lunarDay = [
    { yearDiff: -1, month: 12, date: 30, name: "설날(연휴)" },
    { yearDiff: 0, month: 1, date: 1, name: "설날" },
    { yearDiff: 0, month: 1, date: 2, name: "설날(연휴)" },
    { yearDiff: 0, month: 4, date: 8, name: "석가탄신일" },
    { yearDiff: 0, month: 8, date: 14, name: "추석(연휴)" },
    { yearDiff: 0, month: 8, date: 15, name: "추석" },
    { yearDiff: 0, month: 8, date: 16, name: "추석(연휴)" },
  ]

  let result = [];
  result.push({date: year + "-01-01", event: {name:"신정", holiday: true}});
  result.push({date: year + "-02-05", event: {name:"복슬이 생일", holiday: false}});
  result.push({date: year + "-03-01", event: {name:"삼일절", holiday: true}});
  result.push({date: year + "-04-05", event: {name:"식목일", holiday: false}});
  result.push({date: year + "-04-19", event: {name:"4·19혁명 기념일", holiday: false}});
  result.push({date: year + "-05-01", event: {name:"노동절", holiday: false}});
  result.push({date: year + "-05-05", event: {name:"어린일날", holiday: true}});
  result.push({date: year + "-05-08", event: {name:"어버이날", holiday: false}});
  result.push({date: year + "-05-15", event: {name:"스승의날", holiday: false}});
  result.push({date: year + "-06-06", event: {name:"현충일", holiday: true}});
  result.push({date: year + "-08-15", event: {name:"광복절", holiday: true}});
  result.push({date: year + "-10-01", event: {name:"국군의 날", holiday: false}});
  result.push({date: year + "-10-03", event: {name:"개천절", holiday: true}});
  result.push({date: year + "-10-09", event: {name:"한글날", holiday: true}});
  result.push({date: year + "-12-25", event: {name:"성탄절", holiday: true}});

  lunarDay.forEach(value =>{
    // 음력 공휴일 처리.
    // month는 0부터 시작
    let lunarDatePattern = moment().year(year + value.yearDiff).month(value.month - 1).date(value.date).solar().format("YYYY-MM-DD")
    result.push({date:lunarDatePattern, event: {name:value.name, holiday: true}});
  });
  return result;
}

export default CalendarUtil;