import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {XBreadCrumbComponent} from './x-bread-crumb.component';
import {NgZorroAntdModule} from 'ng-zorro-antd';
import {RouterModule} from '@angular/router';
import {XBreadCrumbService} from "./x-bread-crumb.service";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        NgZorroAntdModule
    ],
    declarations: [XBreadCrumbComponent],
    providers: [XBreadCrumbService],
    exports: [XBreadCrumbComponent]
})

export class XBreadCrumbModule {
}
