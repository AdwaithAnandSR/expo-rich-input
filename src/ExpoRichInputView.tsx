import React, {
    useEffect,
    useRef,
    forwardRef,
    useImperativeHandle
} from "react";
import { requireNativeViewManager, EventEmitter } from "expo-modules-core";

import * as NativeModule from "./ExpoRichInputModule";
import type {
    ExpoRichInputProps,
    ExpoRichInputRef,
    EditEvent,
    KeyboardActionEvent,
    SelectionChangeEvent
} from "./ExpoRichInput.types";

const NativeView = requireNativeViewManager("ExpoRichInput");

const emitter = new EventEmitter() as any

let globalId = 0;

const RichInput = forwardRef<ExpoRichInputRef, ExpoRichInputProps>(
    ({ onEditEvent, onKeyboardAction, onSelectionChange, ...props }, ref) => {
        const idRef = useRef(++globalId);
        const nativeRef = useRef<any>(null);

        useImperativeHandle(ref, () => ({
            focus: () => NativeModule.focus(nativeRef.current),
            blur: () => NativeModule.blur(nativeRef.current)
        }));

        useEffect(() => {
            const subs = [
                emitter.addListener("onEditEvent", (e: EditEvent) => {
                    if (e.id === idRef.current) onEditEvent?.(e);
                }),
                emitter.addListener(
                    "onKeyboardAction",
                    (e: KeyboardActionEvent) => {
                        if (e.id === idRef.current) onKeyboardAction?.(e);
                    }
                ),
                emitter.addListener(
                    "onSelectionChange",
                    (e: SelectionChangeEvent) => {
                        if (e.id === idRef.current) onSelectionChange?.(e);
                    }
                )
            ];

            return () => subs.forEach(s => s.remove());
        }, []);

        return <NativeView ref={nativeRef} {...props} id={idRef.current} />;
    }
);

export default RichInput;
