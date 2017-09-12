package org.shadow

case class tuple22(_1: Int,_2: Int, _3: Int, _4: Int, _5: Int, _6: Int, _7: Int, _8: Int, _9: Int, _10: Int, _11: Int, _12: Int, _13: Int, _14: Int, _15: Int, _16: Int, _17: Int, _18: Int, _19: Int, _20: Int,_21:Int,_22:Int) {

}

case class tuple23(_1: Int,_2: Int, _3: Int, _4: Int, _5: Int, _6: Int, _7: Int, _8: Int, _9: Int, _10: Int, _11: Int, _12: Int, _13: Int, _14: Int, _15: Int, _16: Int, _17: Int, _18: Int, _19: Int, _20: Int,_21:Int,_22:Int,_23:Int) {

}

object main{
  def main(args: Array[String]): Unit = {


    val tuple1 = tuple22(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22)
    print(tuple1)
    val tuple2 = tuple23(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23)
    print(tuple2)

  }
}