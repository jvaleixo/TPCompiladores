package edu.ufsj.lox;

import java.util.List;

abstract classExpr{
   static class Binary extends Expr
      Binary(Expr left, Token operator, Expr right) {
         this.left = left; 
         this.operator = operator; 
         this.right = right; 
      }

      finalExpr left; 
      finalToken operator; 
      finalExpr right; 
   }
   static class Grouping extends Expr
      Grouping(Expr expression) {
         this.expression = expression; 
      }

      finalExpr expression; 
   }
   static class Literal extends Expr
      Literal(Object value) {
         this.value = value; 
      }

      finalObject value; 
   }
   static class Unary extends Expr
      Unary(Token operator, Expr right) {
         this.operator = operator; 
         this.right = right; 
      }

      finalToken operator; 
      finalExpr right; 
   }
}
