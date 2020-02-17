import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IFactura, Factura } from 'app/shared/model/factura.model';
import { FacturaService } from './factura.service';

@Component({
  selector: 'jhi-factura-update',
  templateUrl: './factura-update.component.html'
})
export class FacturaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    identificacion: [null, [Validators.required]],
    fecha: [null, [Validators.required]],
    monto: [null, [Validators.required]],
    fechaVencimiento: [],
    facturadRealizada: [],
    facturaEnviada: [],
    facturaCobrada: []
  });

  constructor(protected facturaService: FacturaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factura }) => {
      if (!factura.id) {
        const today = moment().startOf('day');
        factura.fecha = today;
        factura.fechaVencimiento = today;
      }

      this.updateForm(factura);
    });
  }

  updateForm(factura: IFactura): void {
    this.editForm.patchValue({
      id: factura.id,
      identificacion: factura.identificacion,
      fecha: factura.fecha ? factura.fecha.format(DATE_TIME_FORMAT) : null,
      monto: factura.monto,
      fechaVencimiento: factura.fechaVencimiento ? factura.fechaVencimiento.format(DATE_TIME_FORMAT) : null,
      facturadRealizada: factura.facturadRealizada,
      facturaEnviada: factura.facturaEnviada,
      facturaCobrada: factura.facturaCobrada
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factura = this.createFromForm();
    if (factura.id !== undefined) {
      this.subscribeToSaveResponse(this.facturaService.update(factura));
    } else {
      this.subscribeToSaveResponse(this.facturaService.create(factura));
    }
  }

  private createFromForm(): IFactura {
    return {
      ...new Factura(),
      id: this.editForm.get(['id'])!.value,
      identificacion: this.editForm.get(['identificacion'])!.value,
      fecha: this.editForm.get(['fecha'])!.value ? moment(this.editForm.get(['fecha'])!.value, DATE_TIME_FORMAT) : undefined,
      monto: this.editForm.get(['monto'])!.value,
      fechaVencimiento: this.editForm.get(['fechaVencimiento'])!.value
        ? moment(this.editForm.get(['fechaVencimiento'])!.value, DATE_TIME_FORMAT)
        : undefined,
      facturadRealizada: this.editForm.get(['facturadRealizada'])!.value,
      facturaEnviada: this.editForm.get(['facturaEnviada'])!.value,
      facturaCobrada: this.editForm.get(['facturaCobrada'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactura>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
