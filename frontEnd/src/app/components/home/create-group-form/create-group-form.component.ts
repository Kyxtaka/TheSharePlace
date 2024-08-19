import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, FormGroup, Validators, ReactiveFormsModule,FormControl, AbstractControl } from '@angular/forms';
import { APIServiceGroup } from '../../../services/data/data.service';
// import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-create-account-form',
  standalone: true,
  imports: [CommonModule, 
    FormsModule, 
    ReactiveFormsModule],
  templateUrl: './create-group-form.component.html',
  styleUrl: './create-group-form.component.css'
})
export class CreateGroupFormComponent implements OnInit {
  groupForm!: FormGroup;

  constructor (
    private APIServiceGroup: APIServiceGroup, 
    // private formBuilder: FormBuilder,
    // private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.groupForm = new FormGroup({
      groupName: new FormControl('', Validators.required),
      groupDescription: new FormControl('', [Validators.required]),
      groupPassword: new FormControl('', Validators.required),
      confirmGroupPassword: new FormControl('', Validators.required),
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');
  
    return password && confirmPassword && password.value !== confirmPassword.value
      ? { mismatch: true }  
      : null;
  }

  onSubmit() {
    if (this.groupForm.valid) {
      const {groupName, groupDescription, groupPassword} = this.groupForm.value;
      this.APIServiceGroup.add(groupName, groupDescription, groupPassword).subscribe({
        next: (response) => {
          console.log("group created successfully:", response);
        },
        error: (error) => {
          console.log("error while creating group:", error);
        }
      })
    }
  }
  
}
