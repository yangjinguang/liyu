import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SideNavComponent} from './side-nav.component';
import {NgZorroAntdModule} from 'ng-zorro-antd';
import {RouterModule} from '@angular/router';
import {SideNavService} from './side-nav.service';

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        NgZorroAntdModule
    ],
    declarations: [SideNavComponent],
    exports: [SideNavComponent],
    providers: [SideNavService]
})
export class SideNavModule {
}
