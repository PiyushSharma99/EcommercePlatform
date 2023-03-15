import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-fail-modal',
  templateUrl: './fail-modal.component.html',
  styleUrls: ['./fail-modal.component.css']
})
export class FailModalComponent {
  @Input() buttonMessage:string;
  @Input() failMessage:string;

  constructor(public activeModal: NgbActiveModal) {
    
  }
}
