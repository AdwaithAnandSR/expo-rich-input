import { registerWebModule, NativeModule } from 'expo';

import { ExpoRichInputModuleEvents } from './ExpoRichInput.types';

class ExpoRichInputModule extends NativeModule<ExpoRichInputModuleEvents> {
  PI = Math.PI;
  async setValueAsync(value: string): Promise<void> {
    this.emit('onChange', { value });
  }
  hello() {
    return 'Hello world! 👋';
  }
}

export default registerWebModule(ExpoRichInputModule, 'ExpoRichInputModule');
