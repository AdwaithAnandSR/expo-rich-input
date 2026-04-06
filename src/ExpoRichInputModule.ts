import { NativeModule, requireNativeModule } from 'expo';

import { ExpoRichInputModuleEvents } from './ExpoRichInput.types';

declare class ExpoRichInputModule extends NativeModule<ExpoRichInputModuleEvents> {
  PI: number;
  hello(): string;
  setValueAsync(value: string): Promise<void>;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<ExpoRichInputModule>('ExpoRichInput');
