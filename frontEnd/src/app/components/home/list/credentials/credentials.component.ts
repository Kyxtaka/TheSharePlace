import { Component, OnInit, ViewChild  } from '@angular/core';
import { GlobalService } from '../../../../services/global/global.service';
import { Router } from '@angular/router';
import { Credential, Group, Platform } from '../../../../services/data/data.service';
import { CommonModule } from '@angular/common';



interface Filter {
  name: string,
  value: any
}

@Component({
  selector: 'app-credentials',
  standalone: true,
  imports: [
    CommonModule,
  ],
  templateUrl: './credentials.component.html',
  styleUrl: './credentials.component.css'
})
export class CredentialsComponent implements OnInit {

  //affichage
  public credentials: Credential[] = [];
  public groups: Group[] =  [];
  public platforms: Platform[] = [];

  public SubFilterGroupItems: Group[] = []
  public SubFilterPlatformItems: Platform[] = []

  filterMenu: Filter[] = [
    {
      name: "None",
      value: "NONE"
    },
    {
      name: "Groups",
      value: this.SubFilterGroupItems
    },
    {
      name: "Platform",
      value: this.SubFilterPlatformItems
    }
  ];


  constructor(
    private globalService: GlobalService,
    private router: Router,
  ) {}

  copyText(text: string): void {
    navigator.clipboard.writeText(text)
      .then(() => {console.log('Text copied to clipboard'); })
      .catch(err => {console.error('Failed to copy text: ', err);});
  }

  ngOnInit(): void {
    if (this.globalService.getCurrentUserData() == null || !this.globalService.getCurrentIsLogged()) {this.router.navigate(['/'])}

    this.globalService.accounts$.subscribe({
      next: (data) => {this.credentials = data},
      error: (error) => {console.log("credential Page error:", error)}
    });

    this.globalService.group$.subscribe({
      next: (data) => {this.groups = data},
      error: (error) => {console.log("credential Page error:", error)}
    });

    this.globalService.platform$.subscribe({
      next: (data) => {this.platforms = data},
      error: (error) => {console.log("credential Page error:", error)}
    });
  }

}
