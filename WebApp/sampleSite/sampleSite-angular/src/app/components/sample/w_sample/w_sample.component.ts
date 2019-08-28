import { Component, ChangeDetectorRef, Renderer2, ElementRef, ViewEncapsulation} from "@angular/core";
import { BaseControlComponent, LengthConverter} from "@mobilize/powercomponents";
import { dataTransfer} from "@mobilize/base-components";
@Component({
   selector : 'sample-w_sample',
   templateUrl : './w_sample.component.html',
   styleUrls : ['./w_sample.component.scss'],
   encapsulation : ViewEncapsulation.None
})
@dataTransfer(['mobsample_sample_w_sample'])
export class w_sampleComponent extends BaseControlComponent {
   constructor (changeDetector : ChangeDetectorRef,render2 : Renderer2,elem : ElementRef,lengthConverter : LengthConverter) {
      super(changeDetector,render2,elem,lengthConverter);
   }
}