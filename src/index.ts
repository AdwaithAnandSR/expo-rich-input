// Reexport the native module. On web, it will be resolved to ExpoRichInputModule.web.ts
// and on native platforms to ExpoRichInputModule.ts
export { default } from './ExpoRichInputModule';
export { default as ExpoRichInputView } from './ExpoRichInputView';
export * from  './ExpoRichInput.types';
