package zeab.heapmonitor

case class HeapLog (
                     heapSize: Long,
                     heapMaxSize: Long,
                     heapFreeSize: Long,
                     usedHeap: Long
                   )
