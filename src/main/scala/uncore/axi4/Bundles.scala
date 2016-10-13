// See LICENSE for license details.

package uncore.axi4

import Chisel._
import chisel3.util.Irrevocable
import util.GenericParameterizedBundle

abstract class AXI4BundleBase(params: AXI4BundleParameters) extends GenericParameterizedBundle(params)

abstract class AXI4BundleA(params: AXI4BundleParameters) extends AXI4BundleBase(params)
{
  val id     = UInt(width = params.idBits)
  val addr   = UInt(width = params.addrBits)
  val len    = UInt(width = params.lenBits)  // number of beats - 1
  val size   = UInt(width = params.sizeBits) // bytes in beat = 2^size
  val burst  = UInt(width = params.burstBits)
  val lock   = UInt(width = params.lockBits)
  val cache  = UInt(width = params.cacheBits)
  val prot   = UInt(width = params.protBits)
  val qos    = UInt(width = params.qosBits)  // 0=no QoS, bigger = higher priority
  // val region = UInt(width = 4) // optional
}

// A non-standard bundle that can be both AR and AW
class AXI4BundleARW(params: AXI4BundleParameters) extends AXI4BundleA(params)
{
  val wen = Bool()
}

class AXI4BundleAW(params: AXI4BundleParameters) extends AXI4BundleA(params)
class AXI4BundleAR(params: AXI4BundleParameters) extends AXI4BundleA(params)

class AXI4BundleW(params: AXI4BundleParameters) extends AXI4BundleBase(params)
{
  // id ... removed in AXI4
  val data = UInt(width = params.dataBits)
  val strb = UInt(width = params.dataBits/8)
  val last = Bool()
}

class AXI4BundleR(params: AXI4BundleParameters) extends AXI4BundleBase(params)
{
  val id   = UInt(width = params.idBits)
  val data = UInt(width = params.dataBits)
  val resp = UInt(width = params.respBits)
  val last = Bool()
}

class AXI4BundleB(params: AXI4BundleParameters) extends AXI4BundleBase(params)
{
  val id   = UInt(width = params.idBits)
  val resp = UInt(width = params.respBits)
}

class AXI4Bundle(params: AXI4BundleParameters) extends AXI4BundleBase(params)
{
  val aw = Irrevocable(new AXI4BundleAW(params))
  val w  = Irrevocable(new AXI4BundleW (params))
  val b  = Irrevocable(new AXI4BundleB (params)).flip
  val ar = Irrevocable(new AXI4BundleAR(params))
  val r  = Irrevocable(new AXI4BundleR (params)).flip
}

object AXI4Bundle
{
  def apply(params: AXI4BundleParameters) = new AXI4Bundle(params)
}
