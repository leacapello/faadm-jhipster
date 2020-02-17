import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cliente',
        loadChildren: () => import('./cliente/cliente.module').then(m => m.FaadmClienteModule)
      },
      {
        path: 'direccion',
        loadChildren: () => import('./direccion/direccion.module').then(m => m.FaadmDireccionModule)
      },
      {
        path: 'factura',
        loadChildren: () => import('./factura/factura.module').then(m => m.FaadmFacturaModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class FaadmEntityModule {}
