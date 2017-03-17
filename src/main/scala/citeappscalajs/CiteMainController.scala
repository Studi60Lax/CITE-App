package citeappscalajs
import com.thoughtworks.binding._
import scala.scalajs.js
import org.scalajs.dom.document
import org.scalajs.dom.raw.Event
import org.scalajs.dom.ext.Ajax
import scala.concurrent
.ExecutionContext
.Implicits
.global
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import scala.scalajs.js.annotation.JSExport

@JSExport
object CiteMainController {

	@JSExport
	def main(libUrl: String): Unit = {

		def passOhco2Data: Unit = {
		//	O2Model.corpus = Corpus(CiteMainModel.cexString.get,"\t")
			//O2Controller.updateUserMessage(s"Got data from main controller: ${O2Model.corpus.citedWorks.size} cited works.",0)
			println("skipped passOhco2Data")
		}

		CiteMainController.loadRemoteLibrary(libUrl)

		dom.render(document.body, CiteMainView.mainDiv)
	}

	def updateUserMessage(msg: String, alert: Int): Unit = {
		CiteMainModel.userMessageVisibility := "app_visible"
		CiteMainModel.userMessage := msg
		alert match {
			case 0 => CiteMainModel.userAlert := "default"
			case 1 => CiteMainModel.userAlert := "notice"
			case 2 => CiteMainModel.userAlert := "warn"
		}
		js.timers.setTimeout(9000){ CiteMainModel.userMessageVisibility := "app_hidden" }
	}

	def loadRemoteLibrary(url: String):Unit = {
		Ajax.get(url).onSuccess { case xhr =>
			CiteMainController.updateUserMessage("Got remote library.",0)
			val contents:String = xhr.responseText
			O2Model.updateRepository(contents)
		}
	}

	def loadLocalLibrary(e: Event):Unit = {
		val reader = new org.scalajs.dom.raw.FileReader()
		CiteMainController.updateUserMessage("Loading local library.",0)
		reader.readAsText(e.target.asInstanceOf[org.scalajs.dom.raw.HTMLInputElement].files(0))
		reader.onload = (e: Event) => {
			val contents = reader.result.asInstanceOf[String]

			O2Model.updateRepository(contents)
		}
	}

}
