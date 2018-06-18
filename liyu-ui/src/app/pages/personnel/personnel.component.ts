import {Component, OnInit} from '@angular/core';
import {XBreadCrumbService} from "../../components/x-bread-crumb/x-bread-crumb.service";

@Component({
    selector: 'app-personnel',
    templateUrl: './personnel.component.html',
    styleUrls: ['./personnel.component.less']
})
export class PersonnelComponent implements OnInit {

    constructor(private bcService: XBreadCrumbService) {
    }

    ngOnInit() {
        this.bcService.setItems([
            {
                text: '人员管理'
            }
        ])
    }

}
