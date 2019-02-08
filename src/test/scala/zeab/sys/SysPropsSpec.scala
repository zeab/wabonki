package zeab.sys

//Imports
import org.scalatest.FunSuite

class SysPropsSpec extends FunSuite{

  test("Set and Get Sys Prop as String returns the correct String") {
    //Set the system property
    SysProps.setSysProp("moose", "fox")
    //Get the prop
    val sysProp: String = SysProps.getSysPropAsString("moose")
    assert(sysProp == "fox")
  }

  test("Return the default response if the system prop cannot be found") {
    //Get the prop
    val sysProp: String = SysProps.getSysPropAsString("tiger")
    assert(sysProp == "intentional blank")
  }

  test("Set and Get Sys Prop as String returns the correct Boolean") {
    //Set the system property
    SysProps.setSysProp("llama", "true")
    //Get the prop
    val sysProp: Boolean = SysProps.getSysPropAsBoolean("llama")
    assert(sysProp)
  }

}
