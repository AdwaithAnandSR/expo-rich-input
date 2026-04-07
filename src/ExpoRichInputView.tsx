import React, {
  forwardRef,
  useImperativeHandle,
  useRef
} from 'react'
import { requireNativeViewManager } from 'expo-modules-core'
import * as NativeModule from './ExpoRichInputModule'
import type {
  ExpoRichInputProps,
  ExpoRichInputRef
} from './ExpoRichInput.types'

const NativeView = requireNativeViewManager('ExpoRichInput')

const RichInput = forwardRef<ExpoRichInputRef, ExpoRichInputProps>(
  ({ ...props }, ref) => {
    const nativeRef = useRef<any>(null)

    useImperativeHandle(ref, () => ({
      focus: () => NativeModule.focus(nativeRef.current),
      blur: () => NativeModule.blur(nativeRef.current),
    }))

    return <NativeView ref={nativeRef} {...props} />
  }
)

export default RichInput