import { Component, ChangeDetectorRef, Renderer2, ElementRef, ViewEncapsulation} from "@angular/core";
import { BaseControlComponent, DataManagerEventService, LengthConverter} from "@mobilize/powercomponents";
import { dataTransfer} from "@mobilize/base-components";
@Component({
   selector : 'sample-d_sample_list',
   templateUrl : './d_sample_list.component.html',
   styleUrls : ['./d_sample_list.component.scss'],
   encapsulation : ViewEncapsulation.None
})
@dataTransfer(['sample/sample/d_sample_list/d_sample_list'])
export class d_sample_listComponent extends BaseControlComponent {
   constructor (changeDetector : ChangeDetectorRef,render2 : Renderer2,elem : ElementRef,lengthConverter : LengthConverter,private emmiter : DataManagerEventService = null) {
      super(changeDetector,render2,elem,lengthConverter,emmiter);
   }
}