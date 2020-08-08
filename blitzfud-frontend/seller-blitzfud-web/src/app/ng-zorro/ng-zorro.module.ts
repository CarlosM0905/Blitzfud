import { NgModule } from '@angular/core';


//input
import { NzInputModule } from 'ng-zorro-antd/input';

// Icons
import { NZ_ICONS } from 'ng-zorro-antd/icon';
import { NZ_I18N, es_ES } from 'ng-zorro-antd/i18n';
import { IconDefinition } from '@ant-design/icons-angular';
import * as AllIcons from '@ant-design/icons-angular/icons';

// Modules
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import { NzDatePickerModule } from 'ng-zorro-antd/date-picker';
import { NzTypographyModule } from 'ng-zorro-antd/typography';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { NzModalModule } from 'ng-zorro-antd/modal';
import { NzAvatarModule } from 'ng-zorro-antd/avatar';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzPopoverModule } from 'ng-zorro-antd/popover';
import { NzDrawerModule } from 'ng-zorro-antd/drawer';
import { NzUploadModule } from 'ng-zorro-antd/upload';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzPopconfirmModule } from 'ng-zorro-antd/popconfirm';
import { NzProgressModule } from 'ng-zorro-antd/progress';
import { NzNotificationModule } from 'ng-zorro-antd/notification';
import { NzSkeletonModule } from 'ng-zorro-antd/skeleton';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzCarouselModule } from 'ng-zorro-antd/carousel';
import { NzDividerModule } from 'ng-zorro-antd/divider';
import { NzTabsModule } from 'ng-zorro-antd/tabs';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzCheckboxModule } from 'ng-zorro-antd/checkbox';
import { NzToolTipModule } from 'ng-zorro-antd/tooltip';
import { NzElementPatchModule } from 'ng-zorro-antd/core/element-patch';
import { NzSwitchModule } from 'ng-zorro-antd/switch';



const antDesignIcons = AllIcons as {
  [key: string]: IconDefinition;
};
const icons: IconDefinition[] = Object.keys(antDesignIcons).map(
  (key) => antDesignIcons[key]
);

// NgZorro Components
const NG_ZORRO_COMPONENTS = [
    NzIconModule,
    NzButtonModule,
    NzDropDownModule,
    NzDatePickerModule,
    NzTypographyModule,
    NzInputModule,
    NzFormModule,
    NzTableModule,
    NzMenuModule,
    NzModalModule,
    NzAvatarModule,
    NzLayoutModule,
    NzPopoverModule,
    NzDrawerModule,
    NzUploadModule,
    NzSelectModule,
    NzPopconfirmModule,
    NzProgressModule,
    NzSkeletonModule,
    NzNotificationModule,
    NzTagModule,
    NzCarouselModule,
    NzDividerModule,
    NzTabsModule,
    NzCardModule,
    NzCheckboxModule,
    NzToolTipModule,
    NzElementPatchModule,
    NzSwitchModule
];

@NgModule({
  imports: [...NG_ZORRO_COMPONENTS],
  exports: [...NG_ZORRO_COMPONENTS],
  providers: [
    { provide: NZ_I18N, useValue: es_ES },
    { provide: NZ_ICONS, useValue: icons },
  ],
})
export class NgZorroModule {}
