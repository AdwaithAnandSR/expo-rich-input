import { requireNativeView } from 'expo';
import * as React from 'react';

import { ExpoRichInputViewProps } from './ExpoRichInput.types';

const NativeView: React.ComponentType<ExpoRichInputViewProps> =
  requireNativeView('ExpoRichInput');

export default function ExpoRichInputView(props: ExpoRichInputViewProps) {
  return <NativeView {...props} />;
}
