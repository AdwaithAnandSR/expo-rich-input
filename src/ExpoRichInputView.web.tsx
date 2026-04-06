import * as React from 'react';

import { ExpoRichInputViewProps } from './ExpoRichInput.types';

export default function ExpoRichInputView(props: ExpoRichInputViewProps) {
  return (
    <div>
      <iframe
        style={{ flex: 1 }}
        src={props.url}
        onLoad={() => props.onLoad({ nativeEvent: { url: props.url } })}
      />
    </div>
  );
}
