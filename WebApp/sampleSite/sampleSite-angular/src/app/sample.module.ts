
import { NgModule, CUSTOM_ELEMENTS_SCHEMA  } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PowerComponentsModule } from '@mobilize/powercomponents';
import { WebMapService, WebMapModule } from '@mobilize/angularclient';

import * as sample from './components/sample';

@NgModule({
  imports: [
    CommonModule,
    PowerComponentsModule,
    WebMapModule,
  ],
  exports: [
  sample.w_sampleComponent,
  sample.d_sample_listComponent,
    ],
  declarations: [
  sample.w_sampleComponent,
  sample.d_sample_listComponent,
    ],
  entryComponents: [
  sample.w_sampleComponent,
  sample.d_sample_listComponent,
    ],
   providers: [WebMapService],
   schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})
export class sampleModule { }

