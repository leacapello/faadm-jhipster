import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ICliente, Cliente } from 'app/shared/model/cliente.model';
import { ClienteService } from './cliente.service';
import { IDireccion } from 'app/shared/model/direccion.model';
import { DireccionService } from 'app/entities/direccion/direccion.service';
import { IFactura } from 'app/shared/model/factura.model';
import { FacturaService } from 'app/entities/factura/factura.service';

type SelectableEntity = IDireccion | IFactura;

@Component({
  selector: 'jhi-cliente-update',
  templateUrl: './cliente-update.component.html'
})
export class ClienteUpdateComponent implements OnInit {
  isSaving = false;
  direccions: IDireccion[] = [];
  facturas: IFactura[] = [];

  editForm = this.fb.group({
    id: [],
    descripcion: [null, [Validators.required, Validators.minLength(40)]],
    identificacion: [null, [Validators.required, Validators.minLength(5)]],
    identificacionTipo: [null, [Validators.required]],
    direccion: [],
    factura: []
  });

  constructor(
    protected clienteService: ClienteService,
    protected direccionService: DireccionService,
    protected facturaService: FacturaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliente }) => {
      this.updateForm(cliente);

      this.direccionService
        .query({ filter: 'cliente-is-null' })
        .pipe(
          map((res: HttpResponse<IDireccion[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IDireccion[]) => {
          if (!cliente.direccion || !cliente.direccion.id) {
            this.direccions = resBody;
          } else {
            this.direccionService
              .find(cliente.direccion.id)
              .pipe(
                map((subRes: HttpResponse<IDireccion>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IDireccion[]) => (this.direccions = concatRes));
          }
        });

      this.facturaService.query().subscribe((res: HttpResponse<IFactura[]>) => (this.facturas = res.body || []));
    });
  }

  updateForm(cliente: ICliente): void {
    this.editForm.patchValue({
      id: cliente.id,
      descripcion: cliente.descripcion,
      identificacion: cliente.identificacion,
      identificacionTipo: cliente.identificacionTipo,
      direccion: cliente.direccion,
      factura: cliente.factura
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cliente = this.createFromForm();
    if (cliente.id !== undefined) {
      this.subscribeToSaveResponse(this.clienteService.update(cliente));
    } else {
      this.subscribeToSaveResponse(this.clienteService.create(cliente));
    }
  }

  private createFromForm(): ICliente {
    return {
      ...new Cliente(),
      id: this.editForm.get(['id'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      identificacion: this.editForm.get(['identificacion'])!.value,
      identificacionTipo: this.editForm.get(['identificacionTipo'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      factura: this.editForm.get(['factura'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
