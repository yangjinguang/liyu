import {Component, OnInit} from '@angular/core';
import {Account} from "../../models/account";

@Component({
    selector: 'app-profile-view',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.less']
})
export class ProfileComponent implements OnInit {
    public profile: Account;

    constructor() {
    }

    ngOnInit() {
    }

}
