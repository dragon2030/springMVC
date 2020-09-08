/**
 * 用户页面自动清理缓存
 */
async function asyncPrint(ms) {

  try {

     console.log('start');

     await timeout(ms);  //这里返回了错误

     console.log('end');  //所以这句代码不会被执行了

  } catch(err) {

     console.log(err); //这里捕捉到错误error

  }

}