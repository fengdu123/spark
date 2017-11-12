package com.shadow.jvm.concurent

object DirtyRead {
  @throws[Exception]
  def main(args: Array[String]): Unit = {
    val dr = new DirtyRead
    val t1 = new Thread(new Runnable() {
      override def run(): Unit = dr.setValue("z3", "456")
    })
    t1.start()
    Thread.sleep(1000)
    dr.getValue()
  }
}

class DirtyRead {
  private var username = "shadow"
  private var password = "123"

  def setValue(username: String, password: String): Unit = {
    this.username = username
    try Thread.sleep(2000)
    catch {
      case e: InterruptedException =>
        e.printStackTrace()
    }
    this.password = password
    System.out.println("setValue最终结果：username = " + username + " , password = " + password)
  }

  /**
    * synchronized 防止脏读，set和get都加对象锁，表明一个对象只能同时执行一个获得的锁放的方法
    */
  def getValue(): Unit = System.out.println("getValue方法得到：username = " + this.username + " , password = " + this.password)
}