import {Component, Input, OnInit} from '@angular/core';
import {XBreadCrumbItem} from './x-bread-crumb-item';
import {XBreadCrumbService} from "./x-bread-crumb.service";

@Component({
    selector: 'app-x-bread-crumb',
    templateUrl: './x-bread-crumb.component.html',
    styleUrls: ['./x-bread-crumb.component.less']
})
export class XBreadCrumbComponent implements OnInit {
    public items: XBreadCrumbItem[];

    constructor(private bcService: XBreadCrumbService) {
    }

    ngOnInit() {
        this.bcService.itemsChange().subscribe(result => {
            console.log(result);
            this.items = result;
        })
    }

}
