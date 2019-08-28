import { Component, HostListener } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { WebMapService } from '@mobilize/angularclient';
import { SendV5, Send, Model, RequestConfig, ActionModel } from '@mobilize/webmap-core';
import { Action } from 'rxjs/internal/scheduler/Action';
import { UpdateCurrentKeyService } from '@mobilize/powercomponents/dist/services';

@Component({
  selector: 'app-component',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';

  constructor(private webmapService: WebMapService,
    private updateCurrentKeyService: UpdateCurrentKeyService,
    private activatedRoute: ActivatedRoute) {
    this.activatedRoute.queryParams.subscribe(params => {
      const appName = params['appName'];
      webmapService.init(appName);
      updateCurrentKeyService.registerEvent();
    });
  }

  @HostListener('document:keydown', ['$event'])
  onKeyDown(ev: KeyboardEvent) {
    this.updateCurrentKeyService.setCurrentKey(`${ev.which}`);
  }
}
