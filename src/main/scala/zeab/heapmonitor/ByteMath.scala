package zeab.heapmonitor

trait ByteMath {

  def byteToMegaByte(byte:Long):Long = byte / (1024L * 1024L)

}
