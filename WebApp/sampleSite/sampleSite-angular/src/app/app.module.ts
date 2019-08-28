import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { RootComponent } from './root.component';
import { WebMapModule, WebMapService, WEBMAP_CONFIG  } from '@mobilize/angularclient';
import { PowerComponentsModule } from '@mobilize/powercomponents';
import { sampleModule } from './sample.module';
const config = {
  useDynamicServerEvents: true,
  webMapVersion: 'v4',
  usePercentage: true
};
@NgModule({
declarations: [
AppComponent,
RootComponent
],
imports: [
AppRoutingModule,
BrowserModule,
WebMapModule,
PowerComponentsModule,
sampleModule,
],
providers: [WebMapService , { provide: WEBMAP_CONFIG, useValue: config } ],
bootstrap: [RootComponent],
schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})
export class AppModule { }

