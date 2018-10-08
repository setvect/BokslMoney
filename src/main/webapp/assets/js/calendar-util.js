var CalendarUtil = {};

// 해당 년 주요 기념일 반환
CalendarUtil.getAnniversary = function(year){
  let result = [];
  result.push({date: year + "-01-01", event: {name:"신정", holiday: true}});
  result.push({date: year + "-02-05", event: {name:"복슬이 생일", holiday: false}});
  result.push({date: year + "-03-01", event: {name:"삼일절", holiday: true}});
  result.push({date: year + "-04-05", event: {name:"식목일", holiday: false}});
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
  return result;
}

