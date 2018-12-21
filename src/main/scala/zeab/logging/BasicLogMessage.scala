package zeab.logging

//Imports
import java.text.SimpleDateFormat
import java.util.Calendar

case class BasicLogMessage(
                            msg: String,
                            currentTimestamp: String = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS").format(Calendar.getInstance().getTime)
                          )