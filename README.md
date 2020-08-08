# clj-md2pdf

A Clojure powered CLI tool and library for performing moderately complex Markdown to PDF document. A great tool 
for those who desire simple semi-programmatic document creation. Store your content in Markdown, style your document
with CSS / fonts, set your document properties with readable EDN files.

# Contributors

- Author: Michael Valdron
- Maintainer: Michael Valdron

## Dependency Contribution

- [Open HTML To PDF](https://github.com/danfickle/openhtmltopdf): HTML to PDF Renderer / Core PDF Renderer
- [clj-htmltopdf](https://github.com/gered/clj-htmltopdf): Options data structure concept & CSS helper functions
- [hiccup](https://github.com/weavejester/hiccup): Provided the Hiccup data structure / Provided Hiccup to HTML5 API 
- [markdown-to-hiccup](https://github.com/mpcarolin/markdown-to-hiccup): Markdown to Hiccup data structure APIs
- [Jsoup](https://github.com/jhy/jsoup/): Later injection of styles and fonts / Provides compatibility for HTML5 with *Open HTML to PDF*
- [tools.cli](https://github.com/clojure/tools.cli): CLI argument parsing in Clojure

# License

Copyright © 2020 Michael Valdron

This program and the accompanying materials are made available under the terms of the Eclipse Public License 2.0 which is available at [http://www.eclipse.org/legal/epl-2.0](http://www.eclipse.org/legal/epl-2.0).

This Source Code may also be made available under the following Secondary Licenses when the conditions for such availability set forth in the Eclipse Public License, v. 2.0 are satisfied: GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version, with the GNU Classpath Exception which is available at [https://www.gnu.org/software/classpath/license.html](https://www.gnu.org/software/classpath/license.html).