import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule, FormBuilder, FormGroup, Validators, ReactiveFormsModule, EmailValidator, ValueChangeEvent, FormControl, AbstractControl, FormArray } from '@angular/forms';
import { GlobalService } from '../../../services/global/global.service';
import { AuthService } from '../../../services/auth/auth.service';
import { TokenUtilService } from '../../../services/token/token-util.service';
import { APIServiceAccount, Group } from '../../../services/data/data.service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-create-account-form',
  standalone: true,
  imports: [CommonModule, 
    FormsModule, 
    ReactiveFormsModule],
  templateUrl: './create-account-form.component.html',
  styleUrl: './create-account-form.component.css'
})
export class CreateAccountFormComponent implements OnInit {
  accountForm!: FormGroup;
  groups: Group[] = []
  selectedGroups: Group[] = [];
  constructor (
    private APIServiceAccount: APIServiceAccount, 
    private router: Router, 
    private globalService: GlobalService,
    private formBuilder: FormBuilder, 
    private tokenService: TokenUtilService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.globalService.group$.subscribe({
      next: (data) => {
        this.groups = data
        // this.addCheckboxes();
        // this.cdr.detectChanges();
      },
      error: (error) => {
        console.log("error while geting group in createForm Account:", error);
      }
    })
    this.groups = this.globalService.getCurrentGroupsArray();
    this.accountForm = new FormGroup({
      username: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.email, Validators.required]),
      password: new FormControl('', Validators.required),
      confirmPassword: new FormControl('', Validators.required),
      A2F: new FormControl(false), 
      // group: this.formBuilder.array([])
    }, { validators: this.passwordMatchValidator });
  }

  // private addCheckboxes() {
  //   this.groups.forEach(() => this.groupFormArray.push(new FormControl(false)));
  // }
  
  // get groupFormArray() {
  //   return this.accountForm.get('group') as FormArray;
  // }

  // onGroupChange(event: any, group: Group): void {
  //   if (event.target.checked) {
  //     const copy = group;
  //     console.log("checked box:",copy);
  //     console.log("selected group:", this.selectedGroups)
  //     this.selectedGroups.push(group);
  //   } else {
  //     console.log("unchecked box:",group.name);
  //     const index = this.selectedGroups.indexOf(group);
  //     if (index !== -1) {
  //       this.selectedGroups.splice(index, 1);
  //     }
  //   }
  // }


  passwordMatchValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');
  
    return password && confirmPassword && password.value !== confirmPassword.value
      ? { mismatch: true }  
      : null;
  }

  onSubmit() {
    if (this.accountForm.valid) {
      const {username, email, password, A2F }= this.accountForm.value;
      let  setA2F: string = "false"
      if (A2F) {setA2F = "true"}
      this.APIServiceAccount.add(username, email, A2F, password).subscribe({
        next: (response) => {
          console.log("account shared successfully:", response);
        },
        error: (error) => {
          console.log("error while creating account:", error);
        }
      })
    }
  }
  
}
